package com.example.projectmap

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class ProjectMapApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
            Log.d("ProjectMapApp", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("ProjectMapApp", "Firebase initialization failed: ${e.message}")
        }
    }
}
