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
import com.mucahitdaglioglu.obsapp.admin.Lesson
import com.mucahitdaglioglu.obsapp.databinding.FragmentStudentLessonListBinding

class StudentLessonListFragment : Fragment() {

    private lateinit var binding: FragmentStudentLessonListBinding

    private lateinit var toolbarMain: Toolbar

    private lateinit var firestore: FirebaseFirestore
    private lateinit var lessonList: ArrayList<Lesson>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentLessonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationViewUi()
        lessonList()
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
                    val transition = StudentLessonListFragmentDirections.actionStudentLessonListFragmentToStudentHomeFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.noteList -> {
                    val transition = StudentLessonListFragmentDirections.actionStudentLessonListFragmentToStudentNoteListFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.foodList -> {
                    val transition = StudentLessonListFragmentDirections.actionStudentLessonListFragmentToStudentFoodListFragment(user)
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

    fun lessonList() {
        binding.recyclerViewStudentLessonList.setHasFixedSize(true)
        binding.recyclerViewStudentLessonList.layoutManager = LinearLayoutManager(context)

        lessonList = ArrayList<Lesson>()

        firestore.collection("Lesson").get().addOnSuccessListener {
            for (doc in it) {
                val taskModel = doc.toObject(Lesson::class.java)
                lessonList.add(taskModel)
            }
            binding.recyclerViewStudentLessonList.adapter = StudentLessonListRecyclerViewAdapter(lessonList)
        }
    }

}