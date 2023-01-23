package com.mucahitdaglioglu.obsapp

import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity


class MyToolbar() {

    private lateinit var toggle: ActionBarDrawerToggle

    fun uiModeConfiguration(toolbarMain: Toolbar, resources: Resources, activity: FragmentActivity?,
                             requireActivity: FragmentActivity) {

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                Log.d("tag","uiMode: UI_MODE_NIGHT_NO")

                (activity as AppCompatActivity?)!!.setSupportActionBar(toolbarMain)
                val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
                actionBar?.setDisplayHomeAsUpEnabled(true)

                val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)
                toggle = ActionBarDrawerToggle(requireActivity, drawerMain, toolbarMain, R.string.open, R.string.close)
                toggle.isDrawerIndicatorEnabled = true
                toggle.syncState()

                drawerMain.addDrawerListener(toggle)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                Log.d("tag","uiMode: UI_MODE_NIGHT_YES")

                (activity as AppCompatActivity?)!!.setSupportActionBar(toolbarMain)
                val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
                actionBar?.setDisplayHomeAsUpEnabled(true)

                val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)
                toggle = ActionBarDrawerToggle(requireActivity, drawerMain, toolbarMain, R.string.open, R.string.close)
                toggle.isDrawerIndicatorEnabled = true
                toggle.syncState()

                drawerMain.addDrawerListener(toggle)
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED ->{
                Log.d("tag","uiMode: UI_MODE_NIGHT_UNDEFINED")

                (activity as AppCompatActivity?)!!.setSupportActionBar(toolbarMain)
                val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
                actionBar?.setDisplayHomeAsUpEnabled(true)

                val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)
                toggle = ActionBarDrawerToggle(requireActivity, drawerMain, toolbarMain, R.string.open, R.string.close)
                toggle.isDrawerIndicatorEnabled = true
                toggle.syncState()

                drawerMain.addDrawerListener(toggle)
            }
        }
    }

}