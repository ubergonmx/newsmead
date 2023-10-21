package com.newsmead.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.newsmead.databinding.AccountActivityBinding
import com.newsmead.fragments.account.SignUpFragment

class AccountActivity : AppCompatActivity() {

    private lateinit var viewBinding: AccountActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.viewBinding = AccountActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Display Starting Fragment (Sign Up)
        val signUpFragment = SignUpFragment()
        supportFragmentManager.beginTransaction()
            .replace(this.viewBinding.flAccountContainer.id, signUpFragment)
            .commit()

    }
}