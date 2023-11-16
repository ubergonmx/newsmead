package com.newsmead.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Thread.sleep(1000)
        auth = Firebase.auth
        if (auth.currentUser != null) {
            navigateToMainActivity()
        }
        installSplashScreen()

        // Initialize viewBinding for SplashActivity
        val viewBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Check shared preferences for signup status
        this.sharedPreferences = getSharedPreferences("com.newsmead", Context.MODE_PRIVATE)
        val hasCompletedSignup = this.sharedPreferences.getBoolean("has_completed_signup", false)

        // Navigate to appropriate activity after 3 seconds
        Handler().postDelayed({
            if (hasCompletedSignup) {
                // User has completed signup, navigate to the main part of your app
                navigateToMainActivity()
            } else {
                // User has not completed signup, navigate to the signup process
                val intent = Intent(this, AccountActivity::class.java)
                intent.putExtra("initial_fragment", "sign_up")
                startActivity(intent)
                finish()
            }
        }, 2000)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
