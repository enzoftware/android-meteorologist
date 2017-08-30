package com.hackspace.enzoftware.findmyphone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_trackers.*
import kotlinx.android.synthetic.main.contact_ticket.view.*

class MyTrackers : AppCompatActivity() {
    var adapterTicket:ContactAdapter ?= null
    var contactsList = ArrayList<UserContact>()
    var userData:UserData ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trackers)
        //dummyData()
        userData = UserData(applicationContext)
        adapterTicket = ContactAdapter(this,contactsList)
        lvContacts.adapter = adapterTicket
        lvContacts.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            val userInfo = contactsList[position]
            UserData.myTrackers.remove(userInfo.phone)
            refreshData()

            userData!!.saveContactInformation()

            //remove from db

            val mDataBase = FirebaseDatabase.getInstance().reference
            val userData = UserData(applicationContext)
            mDataBase.child("Users").child(userInfo.phone).child("finders").child(userData.loadPhoneNumber()).removeValue()
        }

        userData!!.loadContactInformation()
        refreshData()
    }


    // Just for debug
    fun dummyData(){
        contactsList.add(UserContact("Enzoftware","994050397"))
        contactsList.add(UserContact("RodrigoGmi2","123456789"))
        contactsList.add(UserContact("Daztery","987456321"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.tracker_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.addContat -> {
                checkPermission()
            }

            R.id.finishActivity -> {
                finish()
            }

            else->{
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    val CONTACT_CODE = 123

    fun checkPermission(){
        if(Build.VERSION.SDK_INT > 23){
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ){
                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS),CONTACT_CODE)
                return
            }
        }

        pickContact()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        when(requestCode){
            CONTACT_CODE->{
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickContact()
                }else{
                    Toast.makeText(this,"Can not access to contact list",Toast.LENGTH_LONG).show()
                }
            }

            else->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    val PICK_CODE = 1234
    fun pickContact(){
        // TODO : GET CONTACT FROM PHONE
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent,PICK_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode){
            PICK_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    val contactData = data!!.data
                    val content = contentResolver.query(contactData,null,null,null,null)

                    if (content.moveToFirst()){
                        val id = content.getString(content.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        val hasPhone = content.getString(content.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                        if (hasPhone.equals("1")){
                            val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id ,null,null)

                            phones.moveToFirst()
                            var phoneNumber = phones.getString(phones.getColumnIndex("data1"))
                            val namePerson = content.getString(content.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                            phoneNumber = UserData.formatPhoneNumber(phoneNumber)
                            UserData.myTrackers.put(phoneNumber,namePerson)
                            refreshData()
                            //contactsList.add(UserContact(namePerson,phoneNumber))
                            //adapterTicket!!.notifyDataSetChanged()
                            /* SAVE TU SHRED REFERENCE */
                            userData!!.saveContactInformation()

                            //save in realtime database

                            val mDataBase = FirebaseDatabase.getInstance().reference
                            val userData = UserData(applicationContext)
                            mDataBase.child("Users").child(phoneNumber).child("finders").child(userData.loadPhoneNumber()).setValue(true)

                        }
                    }
                }
            }
            else->{
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    fun refreshData(){
        contactsList.clear()
        for ((key,value) in UserData.myTrackers){
            contactsList.add(UserContact(value,key))
        }
        adapterTicket!!.notifyDataSetChanged()
    }


    class ContactAdapter:BaseAdapter{
        var context:Context ?= null
        var contactsList = ArrayList<UserContact>()

        constructor(context: Context,contactList: ArrayList<UserContact>){
            this.context = context
            this.contactsList = contactList
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            val userContact = contactsList[p0]
            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contactTicketView = inflater.inflate(R.layout.contact_ticket,null)
            contactTicketView.tvName.text = userContact.name
            contactTicketView.tvPhone.text = userContact.phone

            return contactTicketView

        }

        override fun getItem(p0: Int): Any {
            return contactsList[p0]
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

        override fun getCount(): Int {
            return contactsList.size
        }

    }
}
