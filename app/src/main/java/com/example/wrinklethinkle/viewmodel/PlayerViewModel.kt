package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.PlayerCharacter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PlayerViewModel : ViewModel() {

    fun setPlayerData(player: PlayerCharacter) {
        _playerData.value = player
    }
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val database: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().reference
    }

    private val _playerData = MutableLiveData<PlayerCharacter>()
    val playerData: LiveData<PlayerCharacter> = _playerData

    fun fetchLatestPlayerData() {
        val userId = auth.currentUser?.uid ?: return

        val userRef = database.child("users").child(userId)
        userRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val userData = snapshot.value as? Map<String, Any>
                if (userData != null) {
                    val player = createPlayerFromData(userData)
                    _playerData.postValue(player) // Update LiveData with latest data
                }
            }
        }
    }

    private fun createPlayerFromData(userData: Map<String, Any>): PlayerCharacter {
        val name = userData["name"].toString()
        val level = (userData["level"] as? Long)?.toInt() ?: 1
        val gold = (userData["gold"] as? Long)?.toInt() ?: 0
        val clickPower = (userData["clickPower"] as? Long)?.toDouble() ?: 1.0
        val experience = (userData["experience"] as? Long)?.toInt() ?: 0
        val pesticide = (userData["pesticide"] as? Long)?.toInt() ?: 1
        val fertilizer = (userData["fertilizer"] as? Long)?.toInt() ?: 1
        val flowers = userData["flowers"] as? MutableMap<String, Int> ?: mutableMapOf()
        val seeds = userData["seeds"] as? MutableMap<String, Int> ?: mutableMapOf()
        return PlayerCharacter(name, level, experience, gold, clickPower, pesticide, fertilizer, flowers, seeds)
    }
}