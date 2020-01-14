package com.example.newboardaway.UI.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newboardaway.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class EventFragment : Fragment() {
    private lateinit var eventView: View
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mReference: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        eventView = inflater.inflate(R.layout.fragment_home, container, false)

        //RecyclerView
        mRecyclerView = eventView.findViewById(R.id.recyclerViewFeeds)
        mRecyclerView.setHasFixedSize(true)

        //set layout as Linear Layout
        mRecyclerView.layoutManager = LinearLayoutManager(context)

        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance()
        mReference = mFirebaseDatabase.reference.child("Data")

        return eventView
    }

    //load data into recycler view onStart
    override fun onStart() {
        super.onStart()

        var options = FirebaseRecyclerOptions.Builder<Event>().setQuery(mReference, Event::class.java).build()
        var firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Event, EventViewHolder>(options){

            override fun onBindViewHolder(holder: EventViewHolder, position: Int, event: Event) {
                holder.setDetails(context, event.title, event.image, event.date_Time)

                holder.mView.setOnClickListener {
                    val intent = Intent(context, EventDetails::class.java)
                    intent.putExtra("Firebase_title", event.title)
                    intent.putExtra("Firebase_image", event.image)
                    intent.putExtra("Firebase_dt", event.date_Time)
                    intent.putExtra("Firebase_desc", event.desc)
                    intent.putExtra("Firebase_venue", event.venue)
                    startActivity(intent)
                }

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
                val inflater: LayoutInflater = LayoutInflater.from(parent.context)
                val itemView = inflater.inflate(R.layout.feed_row, parent, false)
                return EventViewHolder(itemView)
            }
        }
        //set adapter to recycler view
        mRecyclerView.adapter = firebaseRecyclerAdapter
        firebaseRecyclerAdapter.startListening()
    }
}