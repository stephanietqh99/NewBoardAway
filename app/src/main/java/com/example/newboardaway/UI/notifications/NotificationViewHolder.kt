package com.example.newboardaway.UI.notifications

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newboardaway.R

class NotificationViewHolder(var mView: View) : RecyclerView.ViewHolder(mView) {

    fun setNotification(
        ctx: Context?,
        title: String?,
        venue: String?,
        date_Time: String?
    ) {
        //Views
        val txtNotifyTitle: TextView = mView.findViewById(R.id.tvNotifTitle)
        val txtNotifyVenue: TextView = mView.findViewById(R.id.tvNotifVenue)
        val txtNotifyDate: TextView = mView.findViewById(R.id.tvNotifDate)
        //set data to views
        txtNotifyTitle.text = title
        txtNotifyVenue.text = venue
        txtNotifyDate.text = date_Time
    }
}