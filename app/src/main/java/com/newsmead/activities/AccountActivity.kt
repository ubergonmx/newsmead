package com.newsmead.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newsmead.R

import com.newsmead.databinding.AccountActivityBinding
import com.newsmead.fragments.account.LogInFragment
import com.newsmead.fragments.account.SignUpFragment

class AccountActivity : AppCompatActivity() {

    private lateinit var viewBinding: AccountActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.viewBinding = AccountActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val intent = intent
        if (intent != null) {
            val initialFragment = intent.getStringExtra("initial_fragment")

            if ("sign_up" == initialFragment) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.flAccountContainer, SignUpFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.flAccountContainer, LogInFragment())
                    .commit()
            }
        }

    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Do Nothing
    }
}