package com.mucahitdaglioglu.obsapp

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mucahitdaglioglu.obsapp.databinding.FragmentRegisterBinding

open class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private lateinit var tcNo: String
    private lateinit var nameAndSurname: String
    private lateinit var telephoneNo: String
    private lateinit var mailAddress: String
    private var degree: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.checkboxStudent.setOnClickListener {
            degree = "Student"
            binding.checkboxLecturer.isChecked = false
        }
        binding.checkboxLecturer.setOnClickListener {
            degree = "Lecturer"
            binding.checkboxStudent.isChecked = false
        }

        binding.btnRegister.setOnClickListener {

            tcNo = binding.editTextRegisterTC.text.toString().trim()
            nameAndSurname = binding.editTextRegisterNameAndSurname.text.toString().trim()
            telephoneNo = binding.editTextRegisterTelephoneNo.text.toString().trim()
            mailAddress = binding.editTextMailAddress.text.toString().trim()
            if (TextUtils.isEmpty(tcNo) || TextUtils.isEmpty(nameAndSurname) ||
                TextUtils.isEmpty(telephoneNo) || TextUtils.isEmpty(mailAddress)) {

                binding.editTextRegisterTC.error = "Lütfen TC No Giriniz"
                binding.editTextRegisterNameAndSurname.error = "Lütfen Adınızı Soyadınızı Giriniz"
                binding.editTextRegisterTelephoneNo.error = "Lütfen Telefon Numaranızı Giriniz"
                binding.editTextMailAddress.error = "Lütfen Mail Adresinizi Giriniz"
                return@setOnClickListener
            }
            else if (degree == "") {
                Toast.makeText(context, "Lütfen Unvanınızı Seçiniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser()
        }

    }


    private lateinit var database: DatabaseReference

    private fun registerUser() {

        database = FirebaseDatabase.getInstance().getReference("Users")
        // val id = database.push().key

        val user = User(tcNo, nameAndSurname, telephoneNo, mailAddress, degree,"false", tcNo)

        database.child(tcNo).setValue(user).addOnCompleteListener {
            Toast.makeText(context, "kayıt başarılı", Toast.LENGTH_LONG).show()

            binding.editTextRegisterTC.text?.clear()
            binding.editTextRegisterNameAndSurname.text?.clear()
            binding.editTextRegisterTelephoneNo.text?.clear()
            binding.editTextMailAddress.text?.clear()

        }.addOnFailureListener { error ->
            Toast.makeText(context, "kayıt yapılamadı: ${error.message}", Toast.LENGTH_LONG).show()
        }

    }



}
