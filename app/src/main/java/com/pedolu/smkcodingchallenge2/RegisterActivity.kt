package com.pedolu.smkcodingchallenge2
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pedolu.smkcodingchallenge2.data.model.UserModel
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity() {
    private lateinit var inputName: String
    private lateinit var inputEmail: String
    private lateinit var inputPassword: String
    private lateinit var session: UserSession
    private lateinit var auth: FirebaseAuth
    lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        ref = FirebaseDatabase.getInstance().reference
        session = UserSession(this)
        btnLogin.setOnClickListener { goToLoginActivity() }
        btnRegister.setOnClickListener { inputValidation() }
    }

    private fun inputValidation() {
        inputName = inpName.text.toString()
        inputEmail = inpEmail.text.toString()
        inputPassword = inpPassword.text.toString()
        when {
            inputName.isEmpty() -> {
                inpName.error = "Nama tidak boleh kosong"
                inpName.requestFocus()
            }
            inputEmail.isEmpty() -> {
                inpEmail.error = "Email tidak boleh kosong"
                inpEmail.requestFocus()
            }
            inputPassword.isEmpty() -> {
                inpPassword.error = "Password tidak boleh kosong"
                inpPassword.requestFocus()
            }
            inputPassword.length < 6 -> {
                inpPassword.error = "Password harus tidak kurang dari 6 karakter"
                inpPassword.requestFocus()
            }
            else -> {
                registerUser(inputEmail, inputPassword)
//                session.addUser(inputUsername, inputPassword)
//                goToMainActivity()
            }
        }

    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeUser()
                    goToMainActivity()
                } else {
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        inpPassword.error = "Password anda terlalu lemah"
                        inpPassword.requestFocus()
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        inpEmail.error = "Email Anda tidak valid"
                        inpEmail.requestFocus()
                    } catch (e: FirebaseAuthUserCollisionException) {
                        inpEmail.error = "Email telah digunakan pengguna lain"
                        inpEmail.requestFocus()
                    } catch (e: Exception) {
                        Log.e("error", e.message)
                    }
                }
            })
    }

    private fun storeUser() {
        val User = UserModel(inputName, inputEmail, "", "", "", "")
        val uid = auth.currentUser!!.uid
        ref.child("Users").child(uid).child("Data").setValue(User)
    }

    private fun goToLoginActivity() {
        val i = Intent(this, LoginActivity::class.java)
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
