package com.mucahitdaglioglu.obsapp.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.User

class RecyclerViewCustomAdapter(private val listUsersPendingApproval: List<User>) : RecyclerView.Adapter<RecyclerViewCustomAdapter.ViewHolder>() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var myContext: Context

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tcNo = itemView.findViewById<TextView>(R.id.textViewAdminRecyclerViewTC)
        val btnApprove = itemView.findViewById<ImageButton>(R.id.btnAdminRecyclerViewApprove)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_recyclerview_item_custom_layout, parent, false)
        myContext = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = listUsersPendingApproval[position]
        holder.tcNo.text = "T.C. No: " + itemsViewModel.tcNo.toString()

        // tıklanan itemdeki kullanıcı onaylanıyor (Firebasedeki değeri güncelliyoruz)
        holder.btnApprove.setOnClickListener {
            val hashMap = HashMap<String, String> ()
            hashMap.put("approved","true")

            dbRef = FirebaseDatabase.getInstance().getReference("Users")
            dbRef.child(itemsViewModel.tcNo.toString()).updateChildren(hashMap as Map<String, Any>)
                .addOnSuccessListener{
                    Toast.makeText(myContext, "Kullanıcı onaylandı", Toast.LENGTH_SHORT).show()
                    notifyItemRemoved(position)
                }
        }
    }
    override fun getItemCount(): Int {
        return listUsersPendingApproval.size
    }
}