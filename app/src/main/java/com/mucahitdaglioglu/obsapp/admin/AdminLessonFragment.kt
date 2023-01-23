package com.mucahitdaglioglu.obsapp.admin

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.mucahitdaglioglu.obsapp.LoginFragment
import com.mucahitdaglioglu.obsapp.MainActivity
import com.mucahitdaglioglu.obsapp.MyToolbar
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.databinding.FragmentAdminLessonBinding

class AdminLessonFragment : Fragment() {

    private lateinit var binding: FragmentAdminLessonBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbarMain: Toolbar

    private lateinit var department: String
    private lateinit var courseName: String
    private lateinit var courseLecturer: String
    private lateinit var courseLocation: String

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminLessonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarMain = (activity as AppCompatActivity?)!!.findViewById(R.id.toolbarMain)
        val navViewMain = (activity as AppCompatActivity?)!!.findViewById<NavigationView>(R.id.navViewMain)

        toolbarMain.visibility = View.VISIBLE
        navViewMain.visibility = View.VISIBLE

        MyToolbar().uiModeConfiguration(toolbarMain, resources, activity, requireActivity())

        val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)

        navViewMain.menu.clear()
        navViewMain.inflateMenu(R.menu.admin_menu)


        navViewMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.addAnnouncementFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminLessonFragment_to_adminAnnouncementFragment)
                }
                R.id.addFoodListFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminLessonFragment_to_adminFoodListFragment)
                }
                R.id.userAuthenticationFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminLessonFragment_to_adminUserAuthenticationFragment)
                }
                R.id.logoutAdmin -> {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }
            it.isChecked = true
            drawerMain.closeDrawers()
            true
        }

        binding.btnAddCourse.setOnClickListener {
            department = binding.editTextDepartment.text.toString().trim()
            courseName = binding.editTextCourseName.text.toString().trim()
            courseLecturer = binding.editTextCourseLecturer.text.toString().trim()
            courseLocation = binding.editTextCourseLocation.text.toString().trim()

            if (TextUtils.isEmpty(department) || TextUtils.isEmpty(courseName)
                || TextUtils.isEmpty(courseLecturer) || TextUtils.isEmpty(courseLocation)) {

                binding.editTextDepartment.error = "Boş bırakılamaz"
                binding.editTextCourseName.error = "Boş bırakılamaz"
                binding.editTextCourseLecturer.error = "Boş bırakılamaz"
                binding.editTextCourseLocation.error = "Boş bırakılamaz"
                return@setOnClickListener
            }

            addLesson()
        }

    }


    fun addLesson() {
        db = FirebaseFirestore.getInstance()
        val data = Lesson(department, courseName, courseLecturer, courseLocation)

        db.collection("Lesson")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Ders eklendi.", Toast.LENGTH_SHORT).show()

                binding.editTextDepartment.text?.clear()
                binding.editTextCourseName.text?.clear()
                binding.editTextCourseLecturer.text?.clear()
                binding.editTextCourseLocation.text?.clear()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Ders eklenemedi.", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)
            drawerMain.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

}

data class Lesson(
    var department: String? = null,
    var courseName: String? = null,
    var courseLecturer: String? = null,
    var courseLocation: String? = null
)