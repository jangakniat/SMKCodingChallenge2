package com.pedolu.smkcodingchallenge2

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.pedolu.smkcodingchallenge2.data.model.UserModel
import com.pedolu.smkcodingchallenge2.util.tampilToast
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.progress_overlay.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var age: String
    private lateinit var gender: String
    private lateinit var telp: String
    private lateinit var address: String
    private lateinit var ref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var image: Uri

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        retrieveUserData()
        auth = FirebaseAuth.getInstance()
        btnSaveEdit.setOnClickListener { updateUserData() }
    }

    private fun retrieveUserData() {
        val intentData = intent.extras
//        val image =Uri.parse( intentData?.getString("image"))
        name = intentData?.getString("name").toString()
        gender = intentData?.getString("gender").toString()
        age = intentData?.getString("age").toString()
        telp = intentData?.getString("telp").toString()
        address = intentData?.getString("address").toString()
        txtName.setText(name)
        txtAge.setText(age)
        setGenderSpinner(gender)
        txtTelephone.setText(telp)
        txtAddress.setText(address)
    }

    private fun setGenderSpinner(string: String) {
        genderSpinner.background.setColorFilter(
            resources.getColor(R.color.colorWhite),
            PorterDuff.Mode.SRC_ATOP
        )
        var x = 0
        while (x < genderSpinner.adapter.count) {
            if (genderSpinner.getItemAtPosition(x).toString() == string) {
                genderSpinner.setSelection(x)
            }
            x++
        }
    }

    private fun updateUserData() {
//        val inputImage = image.toString()
        progressBarOverlay.visibility = View.VISIBLE
        name = txtName.text.toString()
        gender = genderSpinner.selectedItem.toString()
        age = txtAge.text.toString()
        telp = txtTelephone.text.toString()
        address = txtAddress.text.toString()
        inputValidation()
    }

    private fun inputValidation() {
        when {
            name.isEmpty() -> txtName.error = "Nama tidak boleh kosong"
            age.isEmpty() -> txtAge.error = "Umur tidak boleh kosong"
            telp.isEmpty() -> txtTelephone.error = "Telp tidak boleh kosong"
            address.isEmpty() -> txtAddress.error = "Alamat tidak boleh kosong"
            else -> {
                val User = UserModel(name, gender, age, telp, address, "")
                val uid: String = auth.currentUser!!.uid
                ref = FirebaseDatabase.getInstance().getReference("Users")
                ref.child(uid).child("Data").setValue(User)
                    .addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            tampilToast(this, "Sukses Mengedit Profile")
                            completeEditProfileActivity()
                        } else {
                            tampilToast(this, "Coba Lagi")
                            try {
                                throw task.exception!!
                            } catch (e: Exception) {
                                Log.e("error", e.message)
                            }
                        }
                    })
            }
        }
    }

    private fun completeEditProfileActivity() {
        progressBarOverlay.visibility = View.VISIBLE
        val result = Intent()
        result.putExtra("name", name)
        result.putExtra("age", age)
        result.putExtra("gender", gender)
        result.putExtra("telp", telp)
        result.putExtra("address", address)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
