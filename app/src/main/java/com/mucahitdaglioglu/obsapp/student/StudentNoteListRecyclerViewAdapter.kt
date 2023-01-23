package com.mucahitdaglioglu.obsapp.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.lecturer.StudentLessonNote

class StudentNoteListRecyclerViewAdapter(private val noteList: List<StudentLessonNote>) : RecyclerView.Adapter<StudentNoteListRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val txtVLessonName = itemView.findViewById<TextView>(R.id.textViewLessonName)
        val txtVLessonNote = itemView.findViewById<TextView>(R.id.textViewLessonNote)
        val txtVLessonLetterGrade = itemView.findViewById<TextView>(R.id.textViewLessonLetterGrade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_recyclerview_item_note, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = noteList[position]

        holder.txtVLessonName.text = "Ders Adı: "+ itemsViewModel.lessonName
        holder.txtVLessonNote.text = "Puan: " + itemsViewModel.studentNote.toString()
        holder.txtVLessonLetterGrade.text = "Harf Notu: " + itemsViewModel.studentLetterGrade
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

}