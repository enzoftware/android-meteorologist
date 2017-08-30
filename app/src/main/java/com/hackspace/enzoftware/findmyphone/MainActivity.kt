package com.hackspace.enzoftware.findmyphone

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_ticket.view.*


class MainActivity : AppCompatActivity() {

    var adapterTicket: MyTrackers.ContactAdapter?= null
    var contactsList = ArrayList<UserContact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = UserData(this)
        userData.loadPhoneNumber()

        dummyData()

        adapterTicket = MyTrackers.ContactAdapter(this, contactsList)
        lvContacts.adapter = adapterTicket
        lvContacts.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
            val userInfo = contactsList[position]
        }
    }

    fun dummyData(){
        contactsList.add(UserContact("Enzoftware","994050397"))
        contactsList.add(UserContact("RodrigoGmi2","123456789"))
        contactsList.add(UserContact("Daztery","987456321"))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.AddItem -> {
                val intent = Intent(this,MyTrackers::class.java)
                startActivity(intent)
            }

            R.id.HelpItem -> {
                //TODO : Implement an activity
            }

            else->{
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    class ContactAdapter: BaseAdapter {
        var context: Context?= null
        var contactsList = ArrayList<UserContact>()

        constructor(context: Context, contactList: ArrayList<UserContact>){
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
