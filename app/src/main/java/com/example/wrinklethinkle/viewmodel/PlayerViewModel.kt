package com.example.wrinklethinkle.viewmodel

import Player
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayerViewModel : ViewModel() {
    private val _playerData = MutableLiveData<Player>()
    val playerData: LiveData<Player> = _playerData

    fun setPlayerData(player: Player) {
        _playerData.value = player
    }
}