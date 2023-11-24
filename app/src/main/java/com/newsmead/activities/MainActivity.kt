package com.newsmead.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.newsmead.R
import com.newsmead.data.DataHelper

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

        // OnDestinationChangedListener
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    this.viewBinding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.searchFragment -> {
                    this.viewBinding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.savedFragment -> {
                    this.viewBinding.bottomNavigationView.visibility = View.VISIBLE
                }
                R.id.profileFragment -> {
                    this.viewBinding.bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    this.viewBinding.bottomNavigationView.visibility = View.GONE
                }
            }
        }
        
    }
}