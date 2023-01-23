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
import com.mucahitdaglioglu.obsapp.databinding.FragmentStudentNoteListBinding
import com.mucahitdaglioglu.obsapp.lecturer.StudentLessonNote

class StudentNoteListFragment : Fragment() {

    private lateinit var binding: FragmentStudentNoteListBinding

    private lateinit var toolbarMain: Toolbar

    private lateinit var firestore: FirebaseFirestore
    private lateinit var noteList: ArrayList<StudentLessonNote>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudentNoteListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigationViewUi()
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
                    val transition = StudentNoteListFragmentDirections.actionStudentNoteListFragmentToStudentHomeFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.lessonList -> {
                    val transition = StudentNoteListFragmentDirections.actionStudentNoteListFragmentToStudentLessonListFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.foodList -> {
                    val transition = StudentNoteListFragmentDirections.actionStudentNoteListFragmentToStudentFoodListFragment(user)
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

        noteList()
    }


    fun noteList() {
        binding.recyclerViewStudentNoteList.setHasFixedSize(true)
        binding.recyclerViewStudentNoteList.layoutManager = LinearLayoutManager(context)

        noteList = ArrayList<StudentLessonNote>()

        val bundle : StudentHomeFragmentArgs by navArgs()
        val user = bundle.user

        firestore.collection("StudentLessonNote").get().addOnSuccessListener {
            for (doc in it) {
                val taskModel = doc.toObject(StudentLessonNote::class.java)
                if (user.tcNo == taskModel.studentTCNo) {
                    noteList.add(taskModel)
                }
            }
            binding.recyclerViewStudentNoteList.adapter = StudentNoteListRecyclerViewAdapter(noteList)
        }
    }



}