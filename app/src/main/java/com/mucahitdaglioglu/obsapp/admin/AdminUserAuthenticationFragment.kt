package com.mucahitdaglioglu.obsapp.admin

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.mucahitdaglioglu.obsapp.*
import com.mucahitdaglioglu.obsapp.R
import com.mucahitdaglioglu.obsapp.databinding.FragmentAdminUserAuthenticationBinding

class AdminUserAuthenticationFragment : Fragment() {

    private lateinit var binding: FragmentAdminUserAuthenticationBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var toolbarMain: Toolbar

    private lateinit var userPendingArrayList: ArrayList<User>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminUserAuthenticationBinding.inflate(inflater, container, false)
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
                    Navigation.findNavController(view).navigate(R.id.action_adminUserAuthenticationFragment_to_adminAnnouncementFragment)
                }
                R.id.addFoodListFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminUserAuthenticationFragment_to_adminFoodListFragment)
                }
                R.id.addLessonFragment -> {
                    Navigation.findNavController(view).navigate(R.id.action_adminUserAuthenticationFragment_to_adminLessonFragment)
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

        pendingUserList()
    }


    fun pendingUserList() {

        dbRef = FirebaseDatabase.getInstance().getReference("Users")

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        userPendingArrayList = arrayListOf<User>()

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userPendingArrayList.clear() // arraylist temizleyip yeniden create ediyoruz (yönetici kullanıcı onayladığında listeyi yeniden oluşturmak için)

                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        Log.d("tag","sncc: ${user!!.tcNo}  -  ${user.approved}")

                        if (user!!.approved == "false" && user.degree != "Admin"){
                            userPendingArrayList.add(user)
                        }
                    }

                    binding.recyclerView.adapter = RecyclerViewCustomAdapter(userPendingArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
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
