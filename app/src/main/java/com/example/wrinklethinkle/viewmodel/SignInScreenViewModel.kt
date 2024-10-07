package com.example.wrinklethinkle.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.wrinklethinkle.model.PlayerCharacter
import kotlinx.coroutines.flow.flow


class SignInScreenViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val _signInResult = MutableLiveData<Boolean>()
    val signInResult: LiveData<Boolean> = _signInResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // LiveData to hold Player object
    private val _playerData = MutableLiveData<PlayerCharacter>()
    val playerData: LiveData<PlayerCharacter> = _playerData

    // Sign-in method (same as before)
    fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _signInResult.postValue(true)
                } else {
                    _errorMessage.postValue(task.exception?.message)
                    _signInResult.postValue(false)
                }
            }
    }

    // Fetch user data from Firebase and post Player object to LiveData
    fun fetchUserData(userId: String) {
        val userRef = database.child("users").child(userId)
        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val userData = snapshot.value as? Map<String, Any>
                if (userData != null) {
                    val name = userData["name"].toString()
                    val level = (userData["level"] as? Long)?.toInt() ?: 1
                    val gold = (userData["gold"] as? Long)?.toInt() ?: 0
                    val clickPower = (userData["clickPower"] as? Double) ?: 1.0
                    val experience = (userData["experience"] as? Long)?.toInt() ?: 0
                    val pesticide = (userData["pesticide"] as? Long)?.toInt() ?: 1
                    val fertilizer = (userData["fertilizer"] as? Long)?.toInt() ?: 1
                    var flowers = userData["flowers"] as? MutableMap<String, Int> ?: mutableMapOf()
                    var seeds = userData["seeds"] as? MutableMap<String, Int> ?: mutableMapOf()

                    // Create a Player object with the fetched data
                    val player = PlayerCharacter(name, level, experience, gold, clickPower, pesticide, fertilizer, flowers, seeds)

                    // Post Player object to LiveData
                    _playerData.postValue(player)

                    // Log success
                    Log.d("SignInScreenViewModel", "Player data successfully fetched: $player")
                }
            } else {
                Log.d("SignInScreenViewModel", "User data does not exist.")
            }
        }.addOnFailureListener { exception ->
            Log.e("SignInScreenViewModel", "Error fetching user data: ${exception.message}")
        }
    }
}