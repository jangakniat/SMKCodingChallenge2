package com.pedolu.smkcodingchallenge2

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pedolu.smkcodingchallenge2.data.model.UserModel
import com.pedolu.smkcodingchallenge2.util.tampilToast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private val PREFER_NAME = "userPref"
    private lateinit var inputEmail: String
    private lateinit var inputPassword: String
    private var uName: String? = null
    private var uPassword: String? = null
    private lateinit var session: UserSession
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private val RC_SIGN_IN = 1
    lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        session = UserSession(this)
        checkLogin()
        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
        ref = FirebaseDatabase.getInstance().reference
        btnGoogleLogin.setOnClickListener { GoogleLogin() }
        btnRegister.setOnClickListener { goToRegisterActivity() }
        btnLogin.setOnClickListener { inputValidation() }
    }

    private fun checkLogin() {
        if (session.ifLogin()) {
            goToMainActivity()
        } else if (auth.currentUser != null) {
            goToMainActivity()
        }
    }

    private fun GoogleLogin() {
        progressBar.visibility = View.VISIBLE
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
                storeUser()
                goToMainActivity()
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun storeUser() {
        val user = auth.currentUser
        val User = UserModel(user!!.displayName.toString(), user.email.toString(), "", "", "", "")
        val uid = auth.currentUser!!.uid
        ref.child("Users").child(uid).child("Data").setValue(User)
    }

    private fun inputValidation() {
        inputEmail = inpEmail.text.toString()
        inputPassword = inpPassword.text.toString()
        when {
            inputEmail.isEmpty() -> {
                inpEmail.error = "Username tidak boleh kosong"
                inpEmail.requestFocus()
            }
            inputPassword.isEmpty() -> {
                inpPassword.error = "Password tidak boleh kosong"
                inpPassword.requestFocus()
            }
            else -> {
                loginUser(inputEmail, inputPassword)
            }
        }
//        checkUser()
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    goToMainActivity()
                    Toast.makeText(this, "Successfully Logged in :)", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthException) {
                        var errorCode = (task.exception as FirebaseAuthException?)!!.errorCode
                        when (errorCode) {
                            "ERROR_INVALID_EMAIL" -> {
                                inpEmail.error = "Email Anda tidak valid"
                                inpEmail.requestFocus()
                            }
                            "ERROR_USER_NOT_FOUND" -> {
                                inpEmail.error = "Akun Anda belum terdaftar"
                                inpEmail.requestFocus()
                            }
                            "ERROR_WRONG_PASSWORD" -> {
                                inpPassword.error = "Password anda salah"
                                inpPassword.requestFocus()
                            }
                        }
                        Log.e("error", errorCode)
                    } catch (e: FirebaseTooManyRequestsException) {

                    }
                    Log.e("error", "tomany")

                }

            })
    }

    private fun checkUser() {
        if (sharedPreferences.contains("Name")) {
            uName = sharedPreferences.getString("Name", "")
        }
        if (sharedPreferences.contains("Password")) {
            uPassword = sharedPreferences.getString("Password", "")
        }
        if (this.inputEmail.equals(uName) && this.inputPassword.equals(uPassword)) {
            session.createUserLoginSession(
                uName,
                uPassword
            )
            goToMainActivity()
        } else {
            tampilToast(this, "username/ password salah")
        }
    }

    private fun goToRegisterActivity() {
        val i = Intent(this, RegisterActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        finish()
    }

    private fun goToMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
        finish()
    }

}
