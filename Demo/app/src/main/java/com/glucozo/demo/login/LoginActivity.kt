package com.glucozo.demo.welcome.login


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.glucozo.demo.MainActivity
import com.glucozo.demo.databinding.ActivityLoginBinding
import com.glucozo.demo.sign_up.DataHelper
import com.glucozo.demo.sign_up.SignActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var handler: DataHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handler = DataHelper(this)

        binding.btnLg.setOnClickListener {
           if ( handler.userPresent(binding.inputEmail.text.toString(),binding.inputPass.text.toString())){
               val builder = android.app.AlertDialog.Builder(this)
            builder.setMessage("Login success")
                .setPositiveButton("Ok", { dialogInterface: DialogInterface, i: Int -> goHome() })
                .show()
           }
            else {
               val builder = android.app.AlertDialog.Builder(this)
//
            builder.setMessage("Please check your email or password")
                .setNegativeButton(
                    "Ok",
                    { dialogInterface: DialogInterface, i: Int -> goTo() }
                )
                .show()
           }

        }

        binding.tvSign.setOnClickListener {
            val intent = Intent(this, SignActivity::class.java)
            startActivity(intent)
        }

    }

    private fun goHome() {

        startActivity(Intent(this, MainActivity::class.java))
    }
    private fun goTo() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}
