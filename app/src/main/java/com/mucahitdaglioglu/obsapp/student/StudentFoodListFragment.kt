package com.mucahitdaglioglu.obsapp.student

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.mucahitdaglioglu.obsapp.MainActivity
import com.mucahitdaglioglu.obsapp.MyToolbar
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.admin.FoodList
import com.mucahitdaglioglu.obsapp.databinding.FragmentStudentFoodListBinding

class StudentFoodListFragment : Fragment() {

    private lateinit var binding: FragmentStudentFoodListBinding

    private lateinit var toolbarMain: Toolbar

    private lateinit var firestore: FirebaseFirestore
    private lateinit var foodList: ArrayList<FoodList>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentFoodListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationViewUi()
        foodList()
    }

    fun navigationViewUi () {
        toolbarMain = (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.toolbarMain)
        val navViewMain = (activity as AppCompatActivity?)!!.findViewById<NavigationView>(R.id.navViewMain)

        toolbarMain.visibility = View.VISIBLE
        navViewMain.visibility = View.VISIBLE

        MyToolbar().uiModeConfiguration(toolbarMain, resources, activity, requireActivity())

        val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)

        navViewMain.menu.clear()
        navViewMain.inflateMenu(R.menu.student_menu)

        val bundle : StudentHomeFragmentArgs by navArgs()
        val user = bundle.user

        navViewMain.setNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.announcements -> {
                    val transition = StudentFoodListFragmentDirections.actionStudentFoodListFragmentToStudentHomeFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.lessonList -> {
                    val transition = StudentFoodListFragmentDirections.actionStudentFoodListFragmentToStudentLessonListFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.noteList -> {
                    val transition = StudentFoodListFragmentDirections.actionStudentFoodListFragmentToStudentNoteListFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.logoutStudent ->{
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

    fun foodList() {
        binding.recyclerViewStudentFoodList.setHasFixedSize(true)
        binding.recyclerViewStudentFoodList.layoutManager = LinearLayoutManager(context)

        foodList = ArrayList<FoodList>()

        firestore.collection("FoodList").orderBy("date").get().addOnSuccessListener {
            for (doc in it) {
                val taskModel = doc.toObject(FoodList::class.java)
                foodList.add(taskModel)
            }
            binding.recyclerViewStudentFoodList.adapter = StudentFoodListRecyclerViewAdapter(foodList)
        }
    }



}