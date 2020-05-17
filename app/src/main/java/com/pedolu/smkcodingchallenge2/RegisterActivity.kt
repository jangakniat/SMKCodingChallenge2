package com.pedolu.smkcodingchallenge2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputUsername: String
    private lateinit var inputPassword: String
    private lateinit var session: UserSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        session = UserSession(this)
        btnLogin.setOnClickListener { goToLoginActivity() }
        btnRegister.setOnClickListener { inputValidation() }
    }

    private fun inputValidation() {
        inputUsername = inpUsername.text.toString()
        inputPassword = inpPassword.text.toString()
        when {
            inputUsername.isEmpty() -> inpUsername.error = "Username tidak boleh kosong"
            inputPassword.isEmpty() -> inpPassword.error = "Password tidak boleh kosong"
            else -> {
                session.addUser(inputUsername, inputPassword)
                goToMainActivity()
            }
        }

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
