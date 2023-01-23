package com.mucahitdaglioglu.obsapp.admin

import android.app.DatePickerDialog
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
import com.mucahitdaglioglu.obsapp.databinding.FragmentAdminFoodListBinding
import java.util.Calendar

class AdminFoodListFragment : Fragment() {

    private lateinit var binding: FragmentAdminFoodListBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbarMain: Toolbar

    private lateinit var soup: String
    private lateinit var mainFood: String
    private lateinit var otherFood: String
    private lateinit var sweetAppetizer: String
    private lateinit var date: String

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminFoodListBinding.inflate(inflater, container, false)
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
                    Navigation.findNavController(view).navigate(R.id.action_adminFoodListFragment_to_adminAnnouncementFragment)
                }
                R.id.addLessonFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminFoodListFragment_to_adminLessonFragment)
                }
                R.id.userAuthenticationFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminFoodListFragment_to_adminUserAuthenticationFragment)
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

        binding.btnSendFood.setOnClickListener {
            soup = binding.editTextSoup.text.toString().trim()
            mainFood = binding.editTextMainFood.text.toString().trim()
            otherFood = binding.editTextOtherFood.text.toString().trim()
            sweetAppetizer = binding.editTextSweetAppetizer.text.toString().trim()

            if (TextUtils.isEmpty(soup) || TextUtils.isEmpty(mainFood) ||
                TextUtils.isEmpty(otherFood) || TextUtils.isEmpty(sweetAppetizer)) {

                binding.editTextSoup.error = "Boş bırakılamaz"
                binding.editTextMainFood.error = "Boş bırakılamaz"
                binding.editTextOtherFood.error = "Boş bırakılamaz"
                binding.editTextSweetAppetizer.error = "Boş bırakılamaz"
                return@setOnClickListener
            }
            addFood()
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        date = year.toString() + "-" + (month+1).toString() + "-" + day.toString()

        binding.textViewDate.text = date

        binding.textViewDate.setOnClickListener {
            uiDatePicker()
        }

    }

    fun uiDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        date = year.toString() + "-" + (month+1).toString() + "-" + day.toString()

        val datePickerDialog = DatePickerDialog( requireContext(), { view, year, monthOfYear, dayOfMonth ->
                date = year.toString() + "-" + (monthOfYear+1).toString() + "-" + dayOfMonth.toString()
                binding.textViewDate.text = date
            },
            year,
            month,
            day
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    fun addFood() {
        val data = FoodList(date, soup, mainFood, otherFood, sweetAppetizer)

        firestore.collection("FoodList")
            .add(data)
            .addOnSuccessListener {
                Toast.makeText(context, "Yemek listesi eklendi.", Toast.LENGTH_SHORT).show()

                binding.editTextSoup.text?.clear()
                binding.editTextMainFood.text?.clear()
                binding.editTextOtherFood.text?.clear()
                binding.editTextSweetAppetizer.text?.clear()

            }
            .addOnFailureListener {
                Toast.makeText(context, "Yemek listesi eklenemedi.", Toast.LENGTH_SHORT).show()
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

data class FoodList(
    var date: String? = null,
    var soup: String? = null,
    var mainFood: String? = null,
    var otherFood: String? = null,
    var sweetAppetizer: String? = null,
)