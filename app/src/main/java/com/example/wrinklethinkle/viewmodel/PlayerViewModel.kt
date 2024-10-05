package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.PlayerCharacter

class PlayerViewModel : ViewModel() {
    private val _playerData = MutableLiveData<PlayerCharacter>()
    val playerData: LiveData<PlayerCharacter> = _playerData

    fun setPlayerData(player: PlayerCharacter) {
        _playerData.value = player
    }
}