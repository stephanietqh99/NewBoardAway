package com.example.newboardaway.UI.notifications

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newboardaway.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class NotificationsFragment : Fragment() {

    private lateinit var notifyView: View
    private lateinit var mRecyclerView2: RecyclerView
    private lateinit var mFirebaseDatabase2: FirebaseDatabase
    private lateinit var mReference2: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        notifyView = inflater.inflate(R.layout.fragment_notifications, container, false)

        //RecyclerView
        mRecyclerView2 = notifyView.findViewById(R.id.recyclerViewNotif)
        mRecyclerView2.setHasFixedSize(true)

        //set layout as Linear Layout
        mRecyclerView2.layoutManager = LinearLayoutManager(context)

        //send Query to FirebaseDatabase
        mFirebaseDatabase2 = FirebaseDatabase.getInstance()
        mReference2 = mFirebaseDatabase2.reference.child("Notification")

        return notifyView
    }

    //load data into recycler view onStart
    override fun onStart() {
        super.onStart()

        var options2 = FirebaseRecyclerOptions.Builder<Notification>().setQuery(mReference2, Notification::class.java).build()
        var firebaseRecyclerAdapter2 = object : FirebaseRecyclerAdapter<Notification, NotificationViewHolder>(options2){

            override fun onBindViewHolder(holder: NotificationViewHolder, position: Int, notification: Notification) {
                holder.setNotification(context, notification.notifyTitle, notification.notifyVenue, notification.notifyDate)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
                val inflater: LayoutInflater = LayoutInflater.from(parent.context)
                val itemView = inflater.inflate(R.layout.notification_row, parent, false)
                return NotificationViewHolder(itemView)
            }
            fun deleteItem(position: Int) {
                snapshots.getSnapshot(position).ref.removeValue()
            }
        }
        //set adapter to recycler view
        mRecyclerView2.adapter = firebaseRecyclerAdapter2
        firebaseRecyclerAdapter2.startListening()

        val mIth = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val builder = AlertDialog.Builder(context)
                builder.setCancelable(true)
                builder.setTitle("Delete Event")
                builder.setMessage("Confirm to cancel the event?")
                builder.setPositiveButton("Yes"){ dialog, id ->
                    //perform action if yes
                    firebaseRecyclerAdapter2.deleteItem(viewHolder.adapterPosition)
                    Toast.makeText(context, "Cancel successfully", Toast.LENGTH_SHORT).show()
                }

                builder.setNegativeButton("No"){ dialog, id ->
                    //redirect
                    firebaseRecyclerAdapter2.notifyItemChanged(viewHolder.adapterPosition)
                    dialog.dismiss()//close dialog if no
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }).attachToRecyclerView(mRecyclerView2)
    }
}