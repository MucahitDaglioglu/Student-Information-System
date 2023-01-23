package com.mucahitdaglioglu.obsapp.admin

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.mucahitdaglioglu.obsapp.LoginFragment
import com.mucahitdaglioglu.obsapp.MainActivity
import com.mucahitdaglioglu.obsapp.MyToolbar
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.databinding.FragmentAdminHomeBinding


class AdminHomeFragment : Fragment() {

    private lateinit var binding: FragmentAdminHomeBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbarMain: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarMain = (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.toolbarMain)
        val navViewMain = (activity as AppCompatActivity?)!!.findViewById<NavigationView>(R.id.navViewMain)

        toolbarMain.visibility = View.VISIBLE
        navViewMain.visibility = View.VISIBLE

        MyToolbar().uiModeConfiguration(toolbarMain, resources, activity, requireActivity())

        val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)

        val headerView = navViewMain.getHeaderView(0)
        val navUsername = headerView.findViewById<View>(R.id.textViewUserName) as TextView
        navUsername.text = "Yönetici"

        navViewMain.menu.clear()
        navViewMain.inflateMenu(R.menu.admin_menu)

        navViewMain.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.addAnnouncementFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminAnnouncementFragment)
                }
                R.id.addLessonFragment ->{
                    Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminLessonFragment)
                }
                R.id.addFoodListFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminFoodListFragment)
                }
                R.id.userAuthenticationFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminHomeFragment_to_adminUserAuthenticationFragment)
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

        uiHome()
    }


    fun uiHome() {
        binding.btnAddAnnouncement.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminHomeFragment_to_adminAnnouncementFragment)
        }
        binding.btnAddFoodList.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminHomeFragment_to_adminFoodListFragment)
        }
        binding.btnAddLesson.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminHomeFragment_to_adminLessonFragment)
        }
        binding.btnUserAuthentication.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_adminHomeFragment_to_adminUserAuthenticationFragment)
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