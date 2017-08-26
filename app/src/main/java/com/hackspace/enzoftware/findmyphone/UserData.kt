package com.hackspace.enzoftware.findmyphone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

/**
 * Created by enzoftware on 8/25/17.
 */

class UserData {
    var context:Context ?= null
    var sharedPref:SharedPreferences ?= null
    constructor(context:Context){
        this.context = context
        this.sharedPref = context.getSharedPreferences("userData",Context.MODE_PRIVATE)
    }

    fun savePhoneNumber(numberPhone:String){
        var editor = sharedPref!!.edit()
        editor.putString("numberPhone",numberPhone)
        editor.commit()
    }

    fun loadPhoneNumber():String{
        val phoneNumber = sharedPref!!.getString("numberPhone","empty")
        if(phoneNumber.equals("empty")){
            val intent = Intent(context,Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(intent)
        }
        return phoneNumber
    }
}