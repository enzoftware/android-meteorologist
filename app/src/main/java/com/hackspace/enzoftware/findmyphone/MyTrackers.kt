package com.hackspace.enzoftware.findmyphone

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_my_trackers.*
import kotlinx.android.synthetic.main.contact_ticket.view.*

class MyTrackers : AppCompatActivity() {
    var adapterTicket:ContactAdapter ?= null
    var contactsList = ArrayList<UserContact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trackers)
        dummyData()
        adapterTicket = ContactAdapter(this,contactsList)
        lvContacts.adapter = adapterTicket
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
                //TODO : Add new contact
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
