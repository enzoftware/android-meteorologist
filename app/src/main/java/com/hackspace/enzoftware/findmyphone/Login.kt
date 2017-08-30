package com.hackspace.enzoftware.findmyphone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class Login : AppCompatActivity() {

    var mAuth:FirebaseAuth ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        singInAnonymously()
    }

    fun singInAnonymously(){
        mAuth!!.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Authentication success.",
                                Toast.LENGTH_SHORT).show()
                        val user = mAuth!!.getCurrentUser()
                    } else {
                        Toast.makeText(applicationContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun registerPhoneNumber(view:View){

        val mDataBase = FirebaseDatabase.getInstance().reference
        val dateFormat = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
        val date = Date()
        val userData = UserData(this)
        userData.savePhoneNumber(editTextPhoneNumber.text.toString())

        mDataBase.child("Users").child(editTextPhoneNumber.text.toString()).child("request").setValue(dateFormat.format(date).toString())
        finish()
    }
}
