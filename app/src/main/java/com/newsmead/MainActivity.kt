package com.newsmead

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.ktx.Firebase
import com.newsmead.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize viewBinding for MainActivity
        this.viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)
    }
}