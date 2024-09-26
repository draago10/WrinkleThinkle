package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignUpScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult: LiveData<Boolean> = _signUpResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    _signUpResult.postValue(true)
                } else {
                    _errorMessage.postValue(task.exception?.message)
                    _signUpResult.postValue(false)
                }

            }
    }

}