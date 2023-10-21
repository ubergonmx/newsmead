package com.newsmead.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newsmead.MainActivity
import com.newsmead.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Check shared preferences for signup status
        val sharedPreferences = getSharedPreferences("your_preferences_key", Context.MODE_PRIVATE)
        val hasCompletedSignup = sharedPreferences.getBoolean("has_completed_signup", false)

        if (hasCompletedSignup) {
            // User has completed signup, navigate to the main part of your app
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // User has not completed signup, navigate to the signup process
            val intent = Intent(this, AccountActivity::class.java)
            intent.putExtra("initial_fragment", "sign_up")
            startActivity(intent)
            finish()
        }
    }
}
