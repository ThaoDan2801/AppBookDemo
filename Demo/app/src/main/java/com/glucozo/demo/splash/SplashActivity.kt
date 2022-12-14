package com.glucozo.demo.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.glucozo.demo.MainActivity
import com.glucozo.demo.welcome.WelcomeActivity
import com.glucozo.demo.welcome.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, WelcomeActivity::class.java))
        // close splash activity
        finish();
    }
}