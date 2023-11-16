package com.newsmead.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.newsmead.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Thread.sleep(1000)
        auth = Firebase.auth
        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        installSplashScreen()

        // Initialize viewBinding for SplashActivity
        val viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Check shared preferences for signup status
        val sharedPreferences = getSharedPreferences("your_preferences_key", Context.MODE_PRIVATE)
        val hasCompletedSignup = sharedPreferences.getBoolean("has_completed_signup", false)

        // Navigate to appropriate activity after 3 seconds
        Handler().postDelayed({
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
        }, 2000)
    }
}
