package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.FlowerType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }


    private val _signUpResult = MutableLiveData<Boolean>()
    val signUpResult: LiveData<Boolean> = _signUpResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun createUser(email: String, password: String, playerName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        val inventory = mutableMapOf<String, Map<String, Any>>()
                        val startFlower = FlowerType.ROSE
                        val startFlowerMap = mapOf(
                             "rank" to startFlower.rank,
                            "location" to startFlower.location,
                            "seedCost" to startFlower.seedCost,
                            "clickMultiplier" to startFlower.clickMultiplier,
                            "sellPrice" to startFlower.sellPrice
                        )
                        inventory[startFlower.name] = startFlowerMap

                        val defaultUserData = hashMapOf(
                            "name" to playerName,
                            "level" to 1,
                            "experience" to 0,
                            "clickPower" to 1.0,
                            "gold" to 0,
                            "inventory" to inventory
                        )

                        database.child("users").child(uid).setValue(defaultUserData)
                            .addOnCompleteListener { saveTask ->
                                if (saveTask.isSuccessful) {
                                    _signUpResult.postValue(true)
                                } else {
                                    _errorMessage.postValue(saveTask.exception?.message)
                                    _signUpResult.postValue(false)
                                }

                            }
                    } else {
                        _errorMessage.postValue("Failed to retrieve user ID.")
                        _signUpResult.postValue(false)
                    }

                } else {
                    _errorMessage.postValue(task.exception?.message)
                    _signUpResult.postValue(false)
                }

            }
    }

}