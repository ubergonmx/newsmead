package com.newsmead

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.ktx.Firebase

import android.content.Intent
import android.content.SharedPreferences
import com.newsmead.activities.AccountActivity


import com.newsmead.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize viewBinding for MainActivity
        this.viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        // Initialize navController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        this.navController = navHostFragment.navController
        val bottomNavigationView = this.viewBinding.bottomNavigationView
        setupWithNavController(bottomNavigationView, navController)

        // Check a condition to navigate to AccountActivity
        if (shouldNavigateToAccountActivity()) {
            navigateToAccountActivity()
        }

    }

    private fun shouldNavigateToAccountActivity(): Boolean {
        // Check a condition, for example, a flag in shared preferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("navigate_to_account_activity", true)
    }

    private fun navigateToAccountActivity() {
        val intent = Intent(this, AccountActivity::class.java)
        intent.putExtra("initial_fragment", "sign_up") // You can use any identifier you prefer
        startActivity(intent)
    }
}