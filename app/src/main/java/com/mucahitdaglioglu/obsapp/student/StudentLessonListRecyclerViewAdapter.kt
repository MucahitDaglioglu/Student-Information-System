package com.mucahitdaglioglu.obsapp.student

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.admin.Lesson

class StudentLessonListRecyclerViewAdapter(private val lessonList: List<Lesson>) : RecyclerView.Adapter<StudentLessonListRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val txtVCourseName = itemView.findViewById<TextView>(R.id.textViewCourseName)
        val txtVCourseLecturer = itemView.findViewById<TextView>(R.id.textViewCourseLecturer)
        val txtVCourseLocation = itemView.findViewById<TextView>(R.id.textViewCourseLocation)
        val txtVCourseDepartment = itemView.findViewById<TextView>(R.id.textViewCourseDepartment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_recyclerview_item_lesson, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = lessonList[position]

        holder.txtVCourseName.text = itemsViewModel.courseName
        holder.txtVCourseLecturer.text = itemsViewModel.courseLecturer
        holder.txtVCourseLocation.text = itemsViewModel.courseLocation
        holder.txtVCourseDepartment.text = itemsViewModel.department
    }

    override fun getItemCount(): Int {
        return lessonList.size
    }

}