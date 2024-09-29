package com.example.wrinklethinkle

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class ProjectGrow: Application() {
    override fun onCreate() {
        super.onCreate()
        if (FirebaseApp.initializeApp(this) == null) {
            Log.e("FirebaseInit", "Firebase initialization failed")
        } else {
            Log.d("FirebaseInit", "Firebase initialized successfully")
        }
    }
}