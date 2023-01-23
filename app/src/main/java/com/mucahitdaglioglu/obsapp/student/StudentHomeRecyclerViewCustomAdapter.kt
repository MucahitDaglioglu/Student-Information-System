package com.mucahitdaglioglu.obsapp.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.admin.Announcement

class StudentHomeRecyclerViewCustomAdapter(private val announcementList: List<Announcement>) : RecyclerView.Adapter<StudentHomeRecyclerViewCustomAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewAnnouncement = itemView.findViewById<TextView>(R.id.textViewAnnouncement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_recyclerview_item_announcement, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = announcementList[position]

        holder.textViewAnnouncement.text = itemsViewModel.announcement.toString()
    }

    override fun getItemCount(): Int {
        return announcementList.size
    }

}