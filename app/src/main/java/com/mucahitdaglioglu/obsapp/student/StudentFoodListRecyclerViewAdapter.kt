package com.mucahitdaglioglu.obsapp.student

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.admin.FoodList

class StudentFoodListRecyclerViewAdapter(private val foodList: List<FoodList>) : RecyclerView.Adapter<StudentFoodListRecyclerViewAdapter.ViewHolder>() {

    private lateinit var context: Context
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textViewDate = itemView.findViewById<TextView>(R.id.textViewStudentRecyclerViewDate)
        val cardview = itemView.findViewById<CardView>(R.id.cardViewStudentRecyclerViewFoodList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_recyclerview_item_food, parent, false)
        context = view.context
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = foodList[position]

        holder.textViewDate.text = itemsViewModel.date

        holder.cardview.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setView(R.layout.dialog_food_list)

            val dialogCreate = builder.create()
            dialogCreate.show()
            dialogCreate.setCancelable(false)
            dialogCreate.setCanceledOnTouchOutside(false)

            dialogCreate.findViewById<ImageButton>(R.id.btnClose).setOnClickListener {
                dialogCreate.dismiss()
            }

            val txtVDayDate = dialogCreate.findViewById<TextView>(R.id.textViewDialogDayDate)
            val txtVSoup = dialogCreate.findViewById<TextView>(R.id.textViewDialogSoup)
            val txtVMainFood = dialogCreate.findViewById<TextView>(R.id.textViewDialogMainFood)
            val txtVOtherFood = dialogCreate.findViewById<TextView>(R.id.textViewDialogOtherFood)
            val txtVSweetAppetizer = dialogCreate.findViewById<TextView>(R.id.textViewDialogSweetAppetizer)

            txtVDayDate.text = itemsViewModel.date
            txtVSoup.text = itemsViewModel.soup
            txtVMainFood.text = itemsViewModel.mainFood
            txtVOtherFood.text = itemsViewModel.otherFood
            txtVSweetAppetizer.text = itemsViewModel.sweetAppetizer
        }

    }

    override fun getItemCount(): Int {
        return foodList.size
    }

}