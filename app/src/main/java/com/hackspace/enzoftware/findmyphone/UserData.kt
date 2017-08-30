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
        val editor = sharedPref!!.edit()
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

    fun saveContactInformation(){
        var listOfTrackers = ""

        for ((key,value) in myTrackers){
            if( listOfTrackers.isEmpty()){
                listOfTrackers = key + "%" + value
            }else{
                listOfTrackers += key + "%" + value
            }
        }

        if (listOfTrackers.isEmpty()){
            listOfTrackers = "empty"
        }

        val editor = sharedPref!!.edit()
        editor.putString("listOfTrackers",listOfTrackers)
        editor.commit()
    }

    fun loadContactInformation(){
        myTrackers.clear()
        val listOfTrackers = sharedPref!!.getString("listOfTrackers","empty")

        if (!listOfTrackers.equals("empty")){
            val userInfo = listOfTrackers.split("%").toTypedArray()
            var i = 0
            while ( i < userInfo.size){
                myTrackers.put(userInfo[i],userInfo[i+1])
                i += 2
            }

        }
    }

    companion object {
        var myTrackers:MutableMap<String,String> = HashMap()

    }
}