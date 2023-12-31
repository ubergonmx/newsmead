package com.newsmead.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.newsmead.databinding.ActivityAccountBinding
import com.newsmead.fragments.account.LogInFragment
import com.newsmead.fragments.account.SignUpFragment

class AccountActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.viewBinding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val intent = intent
        if (intent != null) {
            val initialFragment = intent.getStringExtra("initial_fragment")

            if ("sign_up" == initialFragment) {
                supportFragmentManager.beginTransaction()
                    .replace(viewBinding.flAccountContainer.id, SignUpFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(viewBinding.flAccountContainer.id, LogInFragment())
                    .commit()
            }
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Do Nothing
    }
}