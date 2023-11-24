package com.newsmead.data

import android.app.Application

class NewsMeadApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseHelper.initDatabase(this)
    }
}