package com.mucahitdaglioglu.obsapp.lecturer

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.mucahitdaglioglu.obsapp.MainActivity
import com.mucahitdaglioglu.obsapp.MyToolbar
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.admin.Announcement
import com.mucahitdaglioglu.obsapp.admin.FoodList
import com.mucahitdaglioglu.obsapp.databinding.FragmentLecturerHomeBinding
import com.mucahitdaglioglu.obsapp.student.StudentHomeRecyclerViewCustomAdapter
import java.util.*

class LecturerHomeFragment : Fragment() {

    private lateinit var binding: FragmentLecturerHomeBinding

    private lateinit var toolbarMain: Toolbar
    private lateinit var dbRef: DatabaseReference

    private lateinit var firestore: FirebaseFirestore
    private lateinit var announcementList: ArrayList<Announcement>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLecturerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationViewUi()

        val prefences = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val firstLogin = prefences.getString("firstLogin","yes")

        if (firstLogin == "yes") {
            changePassword()
        }
        announcementList()
        mealOfDay()
    }

    fun navigationViewUi () {
        toolbarMain = (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.toolbarMain)
        val navViewMain = (activity as AppCompatActivity?)!!.findViewById<NavigationView>(R.id.navViewMain)

        toolbarMain.visibility = View.VISIBLE
        navViewMain.visibility = View.VISIBLE

        MyToolbar().uiModeConfiguration(toolbarMain, resources, activity, requireActivity())

        val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)

        val bundle : LecturerHomeFragmentArgs by navArgs()
        val user = bundle.user

        val headerView = navViewMain.getHeaderView(0)
        val navUsername = headerView.findViewById<View>(R.id.textViewUserName) as TextView
        navUsername.text = user.nameAndSurname.toString()

        navViewMain.menu.clear()
        navViewMain.inflateMenu(R.menu.lecturer_menu)

        navViewMain.setNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.listingLesson -> {
                    val transition = LecturerHomeFragmentDirections.actionLecturerHomeFragmentToLecturerLessonListFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.addLecturesNotes -> {
                    val transition = LecturerHomeFragmentDirections.actionLecturerHomeFragmentToLecturerAddNotesFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.logoutLecturer ->{
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }

            it.isChecked = true
            drawerMain.closeDrawers()
            true
        }
    }


    fun changePassword() {
        val prefences = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor = prefences.edit()
        editor.putString("firstLogin","no")
        editor.apply()

        val bundle : LecturerHomeFragmentArgs by navArgs()
        val user = bundle.user

        if (user.tcNo == user.password) {
            val dialogBinding = layoutInflater.inflate(R.layout.dialog_change_password, null)
            val builder = AlertDialog.Builder(context)
            builder.setView(dialogBinding)
            val dialogCreate = builder.create()
            dialogCreate.show()
            dialogCreate.setCancelable(false)
            dialogCreate.setCanceledOnTouchOutside(false)

            val dialogBtnSavePassword = dialogBinding.findViewById<Button>(R.id.btnDialogNewPassword)
            val dialogEditText = dialogBinding.findViewById<EditText>(R.id.editTextDialogNewPassword)

            dialogBtnSavePassword.setOnClickListener {
                if (TextUtils.isEmpty(dialogEditText.text.toString().trim())) {
                    dialogEditText.error = "Boş bırakılamaz"
                    return@setOnClickListener
                }

                val hashMap = HashMap<String, String> ()
                hashMap.put("password", dialogEditText.text.toString().trim())

                dbRef.child(user.tcNo.toString()).updateChildren(hashMap as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Yeni şifreniz kaydedildi.", Toast.LENGTH_SHORT).show()
                    }
                dialogCreate.dismiss()
            }
        }
    }


    fun announcementList() {
        binding.recyclerViewLecturerHome.setHasFixedSize(true)
        binding.recyclerViewLecturerHome.layoutManager = LinearLayoutManager(context)

        announcementList = ArrayList<Announcement>()
        firestore.collection("Announcement-Herkese").get().addOnSuccessListener {
            for (doc in it){
                val taskModel = doc.toObject(Announcement::class.java)
                announcementList.add(taskModel)
            }
                firestore.collection("Announcement-Öğretim Üyeleri").get().addOnSuccessListener {
                for (doc in it){
                    val taskModel = doc.toObject(Announcement::class.java)
                    announcementList.add(taskModel)
                }
                binding.recyclerViewLecturerHome.adapter = StudentHomeRecyclerViewCustomAdapter(announcementList)
            }
        }
    }

    // günün yemeği
    fun mealOfDay() {
        binding.cardViewLecturerFood.setOnClickListener {
            val binding = layoutInflater.inflate(R.layout.dialog_food_list, null)

            val builder = AlertDialog.Builder(context)
            builder.setView(binding)
            val dialogCreate = builder.create()
            dialogCreate.show()
            dialogCreate.setCancelable(false)
            dialogCreate.setCanceledOnTouchOutside(false)

            binding.findViewById<ImageButton>(R.id.btnClose).setOnClickListener {
                dialogCreate.dismiss()
            }

            val txtVDayDate = binding.findViewById<TextView>(R.id.textViewDialogDayDate)
            val txtVSoup = binding.findViewById<TextView>(R.id.textViewDialogSoup)
            val txtVMainFood = binding.findViewById<TextView>(R.id.textViewDialogMainFood)
            val txtVOtherFood = binding.findViewById<TextView>(R.id.textViewDialogOtherFood)
            val txtVSweetAppetizer = binding.findViewById<TextView>(R.id.textViewDialogSweetAppetizer)

            val cal = Calendar.getInstance()
            val dayDate = cal.get(Calendar.YEAR).toString() + "-" + (cal.get(Calendar.MONTH)+1).toString() + "-" + cal.get(Calendar.DAY_OF_MONTH).toString()
            txtVDayDate.text = dayDate

            firestore.collection("FoodList").get().addOnSuccessListener {
                for (doc in it){
                    val taskModel = doc.toObject(FoodList::class.java)
                    if (taskModel.date.toString() == dayDate) {
                        txtVSoup.text = taskModel.soup
                        txtVMainFood.text = taskModel.mainFood
                        txtVOtherFood.text = taskModel.otherFood
                        txtVSweetAppetizer.text = taskModel.sweetAppetizer
                    }
                }
            }
        }
    }






}