package com.newsmead.data

import android.app.Application
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Application class to initialize the Room database. This will be called before any other
 * component is initialized.
 */
class NewsMeadApplication: Application() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        DatabaseHelper.initDatabase(this)

        // Check if logged in
        // If logged in, then preload data
        if (FirebaseHelper.getUid() != "null") {
            GlobalScope.launch {
                val pairData = FirebaseHelper.getListsAndArticles(this@NewsMeadApplication)
                PreloadedData.updateSavedData(pairData)
            }
        }
    }
}