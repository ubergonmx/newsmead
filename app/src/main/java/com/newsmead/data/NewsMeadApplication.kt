package com.newsmead.data

import android.app.Application

/**
 * Application class to initialize the Room database. This will be called before any other
 * component is initialized.
 */
class NewsMeadApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseHelper.initDatabase(this)
    }
}