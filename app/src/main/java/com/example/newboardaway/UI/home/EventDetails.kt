package com.example.newboardaway.UI.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newboardaway.R
import com.example.newboardaway.UI.notifications.Notification
import com.google.firebase.FirebaseApp.initializeApp
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.feed_details.*

class EventDetails : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feed_details)

        val bundle: Bundle? = intent.extras
        val image = bundle!!.getString("Firebase_image")
        val title = bundle.getString("Firebase_title")
        val date_Time = bundle.getString("Firebase_dt")
        val desc = bundle.getString("Firebase_desc")
        val venue = bundle.getString("Firebase_venue")

        txtTitle.text = title
        textDesc.text = desc
        txtVenue.text = venue
        txtDT.text = date_Time
        Picasso.get().load(image).into(detailsImageView)

        buttonJoin.setOnClickListener {
            val mRef = FirebaseDatabase.getInstance().getReference("Notification")
            val userId = mRef.push().key
            val notification = Notification(title, venue, date_Time)
            initializeApp(this)

            mRef.child(title.toString()).setValue(notification).addOnCompleteListener {
                Toast.makeText(applicationContext,"Join successfully.", Toast.LENGTH_LONG).show()
            }
        }
    }
}