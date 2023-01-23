package com.mucahitdaglioglu.obsapp

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.mucahitdaglioglu.obsapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var tcNo: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbarMain = (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.toolbarMain)
        val navViewMain = (activity as AppCompatActivity?)!!.findViewById<NavigationView>(R.id.navViewMain)
        toolbarMain.visibility = View.GONE
        navViewMain.visibility = View.GONE

        // Kayit ol ekranina git
        binding.txtVRegister.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            tcNo = binding.editTextTC.text.toString().trim()
            password = binding.editTextPassword.text.toString().trim()

            if (TextUtils.isEmpty(tcNo) || TextUtils.isEmpty(password)) {
                binding.editTextTC.error = "Lütfen TC No Giriniz"
                binding.editTextPassword.error = "Lütfen Şifrenizi Giriniz"
                return@setOnClickListener
            }

            isLogin()
        }

    }



    private lateinit var toggle: ActionBarDrawerToggle

    private fun isLogin() {
        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child("Users")
            .orderByChild("tcNo").equalTo(binding.editTextTC.text.toString()).addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val password = binding.editTextPassword.text.toString()
                    val prefences = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
                    val editor = prefences.edit()

                    if (snapshot.exists()) {
                        for (user in snapshot.children) {
                            val usersObs: User? = user.getValue(User::class.java)
                            if (usersObs!!.password.equals(password.trim())) {

                                when (usersObs.degree) {
                                    "Admin" -> {
                                        Toast.makeText(context, "admin giriş yaptı", Toast.LENGTH_SHORT).show()
                                        Navigation.findNavController(view!!).navigate(R.id.action_loginFragment_to_adminHomeFragment)
                                        navigationDrawerAndToolbar(R.menu.admin_menu)
                                    }
                                    "Student" -> {
                                        if (usersObs.approved == "true") {
                                            Toast.makeText(context, "Öğrenci giriş yaptı", Toast.LENGTH_SHORT).show()

                                            val usersData = users(usersObs.tcNo, usersObs.nameAndSurname,
                                            usersObs.telephoneNo, usersObs.mailAddress, usersObs.degree, usersObs.approved, usersObs.password)

                                            if (usersObs.tcNo == usersObs.password) {
                                                editor.putString("firstLogin","yes")
                                            } else {
                                                editor.putString("firstLogin","no")
                                            }
                                            editor.apply()

                                            val transition = LoginFragmentDirections.actionLoginFragmentToStudentHomeFragment(usersData)
                                            Navigation.findNavController(view!!).navigate(transition)

                                            navigationDrawerAndToolbar(R.menu.student_menu)
                                        } else {
                                            Toast.makeText(context, "Admin tarafından onaylandığınızda giriş yapabileceksiniz.", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                    else -> {
                                        if (usersObs.approved == "true") {
                                            Toast.makeText(context, "Öğretim üyesi giriş yaptı", Toast.LENGTH_SHORT).show()

                                            val usersData = users(usersObs.tcNo, usersObs.nameAndSurname,
                                                usersObs.telephoneNo, usersObs.mailAddress, usersObs.degree, usersObs.approved, usersObs.password)

                                            if (usersObs.tcNo == usersObs.password) {
                                                editor.putString("firstLogin","yes")
                                            } else {
                                                editor.putString("firstLogin","no")
                                            }
                                            editor.apply()

                                            val transition = LoginFragmentDirections.actionLoginFragmentToLecturerHomeFragment(usersData)
                                            Navigation.findNavController(view!!).navigate(transition)

                                            navigationDrawerAndToolbar(R.menu.lecturer_menu)
                                        } else {
                                            Toast.makeText(context, "Admin tarafından onaylandığınızda giriş yapabileceksiniz.", Toast.LENGTH_SHORT).show()
                                        }

                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Kullanıcı Bulunamadı", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun navigationDrawerAndToolbar(menu: Int) {
        val toolbarMain = (activity as AppCompatActivity?)!!.findViewById<Toolbar>(R.id.toolbarMain)
        val navViewMain = (activity as AppCompatActivity?)!!.findViewById<NavigationView>(R.id.navViewMain)
        toolbarMain.visibility = View.VISIBLE
        navViewMain.visibility = View.VISIBLE
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbarMain)

        navViewMain.menu.clear()
        navViewMain.inflateMenu(menu)

        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)
        toggle = ActionBarDrawerToggle(requireActivity(), drawerMain, toolbarMain, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        drawerMain.addDrawerListener(toggle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            val drawerMain = (activity as AppCompatActivity?)!!.findViewById<DrawerLayout>(R.id.drawerLayoutMain)
            drawerMain.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

}