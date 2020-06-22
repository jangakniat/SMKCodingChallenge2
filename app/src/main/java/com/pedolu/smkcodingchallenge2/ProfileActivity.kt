package com.pedolu.smkcodingchallenge2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.pedolu.smkcodingchallenge2.util.tampilToast
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.progress_overlay.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var age: String
    private lateinit var gender: String
    private lateinit var telp: String
    private lateinit var address: String
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth

    companion object {
        val REQUEST_CODE = 100

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        retrieveUserData()
        btnToEdit.setOnClickListener { goToEditProfileActivity() }
        btnExit.setOnClickListener { logoutUser() }
    }

    private fun retrieveUserData() {
        progressBarOverlay.visibility = View.VISIBLE
        profileLinearLayout.visibility = View.GONE
        auth = FirebaseAuth.getInstance()
        val uid: String = auth.currentUser!!.uid
        ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid).child("Data").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                tampilToast(applicationContext, "Database Error")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                name = dataSnapshot.child("name").value.toString()
                gender = dataSnapshot.child("gender").value.toString()
                age = dataSnapshot.child("age").value.toString()
                telp = dataSnapshot.child("telp").value.toString()
                address = dataSnapshot.child("address").value.toString()
                txtName.text = name
                txtGender.text = gender
                txtAge.text = age
                txtTelephone.text = telp
                txtAddress.text = address
                progressBarOverlay.visibility = View.GONE
                profileLinearLayout.visibility = View.VISIBLE
            }
        })
    }

    private fun goToEditProfileActivity() {
        val intent = Intent(this, EditProfileActivity::class.java)
        intent.putExtra("name", name)
        intent.putExtra("gender", gender)
        intent.putExtra("age", age)
        intent.putExtra("telp", telp)
        intent.putExtra("address", address)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun logoutUser() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                val i = Intent(this, LoginActivity::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
                finish()
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
