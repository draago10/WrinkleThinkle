package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.FlowerType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.wrinklethinkle.model.PlayerCharacter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

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

    private val _playerData = MutableLiveData<PlayerCharacter?>()
    val playerData: LiveData<PlayerCharacter?> = _playerData

    fun createUser(email: String, password: String, playerName: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        val emptyFlowers = mutableMapOf<String, Int>()
                        val defaultSeed = mutableMapOf(
                            "ROSE" to 1
                        )
                        val defaultUserData = hashMapOf(
                            "name" to playerName,
                            "level" to 1,
                            "experience" to 0,
                            "clickPower" to 1.0,
                            "gold" to 0,
                            "pesticide" to 0,
                            "fertilizer" to 0,
                            "flowers" to emptyFlowers,
                            "seeds" to defaultSeed
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

    fun fetchPlayerData(userId: String) {
        val userRef = database.child("users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userData = snapshot.value as? Map<String, Any>
                    if (userData != null) {
                        val player = createPlayerFromData(userData)
                        _playerData.postValue(player)
                    } else {
                        _errorMessage.postValue("Failed to parse user data.")
                        _playerData.postValue(null)
                    }
                } else {
                    _errorMessage.postValue("User data does not exist.")
                    _playerData.postValue(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                _errorMessage.postValue("Error fetching user data: ${error.message}")
                _playerData.postValue(null)
            }
        })
    }

    private fun createPlayerFromData(userData: Map<String, Any>): PlayerCharacter {
        val name = userData["name"].toString()
        val level = (userData["level"] as? Long)?.toInt() ?: 1
        val gold = (userData["gold"] as? Long)?.toInt() ?: 0
        val clickPower = (userData["clickPower"] as? Double) ?: 1.0
        val experience = (userData["experience"] as? Long)?.toInt() ?: 0
        val pesticide = (userData["pesticide"] as? Long)?.toInt() ?: 1
        val fertilizer = (userData["fertilizer"] as? Long)?.toInt() ?: 1
        val flowers = userData["flowers"] as? MutableMap<String, Int> ?: mutableMapOf()
        val seeds = userData["seeds"] as? MutableMap<String, Int> ?: mutableMapOf()
        return PlayerCharacter(name, level, experience, gold, clickPower, pesticide, fertilizer, flowers, seeds)
    }
}

