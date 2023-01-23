package com.mucahitdaglioglu.obsapp.admin

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.mucahitdaglioglu.obsapp.MainActivity
import com.mucahitdaglioglu.obsapp.MyToolbar
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.databinding.FragmentAdminAnnouncementBinding


class AdminAnnouncementFragment : Fragment() {

    private lateinit var binding: FragmentAdminAnnouncementBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbarMain: Toolbar

    private lateinit var db: FirebaseFirestore
    private var announcement = ""

    private var announcementToWhom = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminAnnouncementBinding.inflate(inflater, container, false)
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
                R.id.addLessonFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminAnnouncementFragment_to_adminLessonFragment)
                }
                R.id.addFoodListFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminAnnouncementFragment_to_adminFoodListFragment)
                }
                R.id.userAuthenticationFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminAnnouncementFragment_to_adminUserAuthenticationFragment)
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

        addAnnouncement()
        spinnerFormat()
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

    fun addAnnouncement() {
        binding.btnSendAnnouncement.setOnClickListener {

            announcement = binding.editText.text.toString().trim()

            if (TextUtils.isEmpty(announcement)) {
                Toast.makeText(context, "Lütfen duyuru giriniz." ,Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (TextUtils.isEmpty(announcementToWhom)) {
                binding.autoCompleteTextView.error = "Lütfen kime göndereceğinizi seçiniz"
                return@setOnClickListener
            } else {
                db = FirebaseFirestore.getInstance()
                val data = Announcement(announcement)

                db.collection("Announcement-$announcementToWhom")
                    .add(data)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Duyuru gönderildi.", Toast.LENGTH_SHORT).show()

                        binding.editText.text?.clear()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Duyuru gönderilemedi.", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun spinnerFormat() {
        val list = listOf("Herkese","Öğrenciler","Öğretim Üyeleri")
        val adapter= ArrayAdapter(requireContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, list)

        adapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

        (binding.textInputLayoutAutoComplete.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.autoCompleteTextView.setOnItemClickListener { adapterView, view, position, l ->
            announcementToWhom = adapter.getItem(position).toString()
        }

    }


}

// Veriler bir Map<String, Object> veya uygun bir POJO nesnesi olmalıdır
// (Invalid data. Data must be a Map<String, Object> or a suitable POJO object)
data class Announcement(
    var announcement: String? = null
)