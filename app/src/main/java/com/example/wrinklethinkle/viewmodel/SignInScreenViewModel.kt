package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val _signInResult = MutableLiveData<Boolean>()
    val signInResult: LiveData<Boolean> = _signInResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    _signInResult.postValue(true)
                } else {
                    _errorMessage.postValue(task.exception?.message)
                    _signInResult.postValue(false)
                }
            }
    }
}