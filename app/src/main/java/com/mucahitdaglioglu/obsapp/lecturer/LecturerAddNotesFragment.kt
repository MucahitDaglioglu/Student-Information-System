package com.mucahitdaglioglu.obsapp.lecturer

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.mucahitdaglioglu.obsapp.MainActivity
import com.mucahitdaglioglu.obsapp.MyToolbar
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.admin.Lesson
import com.mucahitdaglioglu.obsapp.databinding.FragmentLecturerAddNotesBinding
import com.mucahitdaglioglu.obsapp.student.StudentLessonListRecyclerViewAdapter
import com.mucahitdaglioglu.obsapp.users

class LecturerAddNotesFragment : Fragment() {

    private lateinit var binding: FragmentLecturerAddNotesBinding

    private lateinit var toolbarMain: Toolbar
    private lateinit var firestore: FirebaseFirestore

    private lateinit var lessonName: String
    private lateinit var studentTCNo: String
    private var studentNote: Int? = null
    private lateinit var studentLetterGrade: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLecturerAddNotesBinding.inflate(inflater, container, false)
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
        navViewMain.inflateMenu(R.menu.lecturer_menu)

        val bundle : LecturerAddNotesFragmentArgs by navArgs()
        val user = bundle.user

        navViewMain.setNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.announcementLecturer -> {
                    val transition = LecturerAddNotesFragmentDirections.actionLecturerAddNotesFragmentToLecturerHomeFragment(user)
                    Navigation.findNavController(requireView()).navigate(transition)
                }
                R.id.listingLesson -> {
                    val transition = LecturerAddNotesFragmentDirections.actionLecturerAddNotesFragmentToLecturerLessonListFragment(user)
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
        binding.btnAddStudentNote.setOnClickListener {
            lessonName = binding.editTextLessonName.text.toString().trim()
            studentTCNo = binding.editTextStudentTCNo.text.toString().trim()

            if (TextUtils.isEmpty(lessonName) || TextUtils.isEmpty(studentTCNo) || TextUtils.isEmpty(binding.editTextStudentNote.text)) {
                binding.editTextLessonName.error = "Boş bırakılamaz"
                binding.editTextStudentTCNo.error = "Boş bırakılamaz"
                binding.editTextStudentNote.error = "Boş bırakılamaz"
                return@setOnClickListener
            }
            studentNote = binding.editTextStudentNote.text.toString().toInt()
            addNote()
        }

        binding.btnUpdateStudentNote.setOnClickListener {
            lessonName = binding.editTextLessonName.text.toString().trim()
            studentTCNo = binding.editTextStudentTCNo.text.toString().trim()

            if (TextUtils.isEmpty(lessonName) || TextUtils.isEmpty(studentTCNo) || TextUtils.isEmpty(binding.editTextStudentNote.text)) {
                binding.editTextLessonName.error = "Boş bırakılamaz"
                binding.editTextStudentTCNo.error = "Boş bırakılamaz"
                binding.editTextStudentNote.error = "Boş bırakılamaz"
                return@setOnClickListener
            }
            studentNote = binding.editTextStudentNote.text.toString().toInt()
            updateNote()
        }

    }

    fun addNote() {
        calculateLetterGrade()
        val data = StudentLessonNote(lessonName, studentTCNo, studentNote.toString(), studentLetterGrade)

        firestore.collection("StudentLessonNote")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Öğrencinin notu eklendi.", Toast.LENGTH_SHORT).show()
                binding.editTextLessonName.text?.clear()
                binding.editTextStudentTCNo.text?.clear()
                binding.editTextStudentNote.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Öğrencinin notu eklenemedi.", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateNote() {
        calculateLetterGrade()
        val hashMap = HashMap<String, String> ()
        hashMap.put("studentNote", studentNote.toString())
        hashMap.put("studentLetterGrade", studentLetterGrade)

        firestore.collection("StudentLessonNote").get().addOnSuccessListener {
            for (doc in it) {
                firestore.collection("StudentLessonNote").document(doc.id).update(hashMap as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Öğrencinin notu güncellendi.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Öğrencinin notu güncellenemedi.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    fun calculateLetterGrade() {
        if (studentNote!! > 89){
            studentLetterGrade = "AA"
        }
        else if (studentNote!! > 79) {
            studentLetterGrade = "BA"
        }
        else if (studentNote!! > 69) {
            studentLetterGrade = "BB"
        }
        else if (studentNote!! > 59) {
            studentLetterGrade = "CB"
        }
        else if (studentNote!! > 49) {
            studentLetterGrade = "CC"
        }
        else if (studentNote!! > 45) {
            studentLetterGrade = "DC"
        }
        else if (studentNote!! > 39) {
            studentLetterGrade = "DD"
        }
        else {
            studentLetterGrade = "FF"
        }
    }

}

data class StudentLessonNote(
    var lessonName: String? = null,
    var studentTCNo: String? = null,
    var studentNote: String? = null,
    var studentLetterGrade: String? = null,
)