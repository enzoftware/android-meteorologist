package com.hackspace.enzoftware.findmyphone

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    fun registerPhoneNumber(view:View){
        val userData = UserData(this)
        userData.savePhoneNumber(editTextPhoneNumber.text.toString())
        finish()
    }
}
