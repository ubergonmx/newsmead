package com.newsmead

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.crashlytics.ktx.setCustomKeys
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val crashlytics = Firebase.crashlytics
        crashlytics.setCustomKeys {
            key("current_level", 1)
            key("last_UI_action", "empty_state")
        }
        Firebase.crashlytics.log("Test log")
        throw RuntimeException("Test Crash 3")
    }
}