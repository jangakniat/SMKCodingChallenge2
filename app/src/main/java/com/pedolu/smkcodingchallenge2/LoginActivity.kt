package com.pedolu.smkcodingchallenge2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pedolu.smkcodingchallenge2.util.tampilToast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private val PREFER_NAME = "userPref"
    private lateinit var inputUsername: String
    private lateinit var inputPassword: String
    private var uName: String? = null
    private var uPassword: String? = null
    private lateinit var session: UserSession
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        session = UserSession(this)
        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE)
        btnRegister.setOnClickListener { goToRegisterActivity() }
        btnLogin.setOnClickListener { inputValidation() }
    }

    private fun inputValidation() {
        inputUsername = inpUsername.text.toString()
        inputPassword = inpPassword.text.toString()
        when {
            inputUsername.isEmpty() -> inpUsername.error = "Username tidak boleh kosong"
            inputPassword.isEmpty() -> inpPassword.error = "Password tidak boleh kosong"
        }
        checkUser()
    }

    private fun checkUser() {
        if (sharedPreferences.contains("Name")) {
            uName = sharedPreferences.getString("Name", "")
        }
        if (sharedPreferences.contains("Password")) {
            uPassword = sharedPreferences.getString("Password", "")
        }
        if (this.inputUsername.equals(uName) && this.inputPassword.equals(uPassword)) {
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
