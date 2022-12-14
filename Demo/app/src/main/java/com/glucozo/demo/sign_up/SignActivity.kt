package com.glucozo.demo.sign_up

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.glucozo.demo.User
import com.glucozo.demo.databinding.ActivitySignBinding

import com.glucozo.demo.welcome.login.LoginActivity

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignBinding
    lateinit var handler: DataHelper
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = DataHelper(this)
        binding.btnSg.setOnClickListener{
            handler.insertUserData(binding.name.text.toString(),binding.email.text.toString(),binding.pass.text.toString())
            addEvent()
        }

    }
    private fun addEvent() {

        binding.btnSg.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val pass = binding.pass.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Users")
            val User = User(name, email, pass)
            database.child(name).setValue(User).addOnSuccessListener {
                binding.name.text.clear()
                binding.email.text.clear()
                binding.pass.text.clear()

                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }



    }
}