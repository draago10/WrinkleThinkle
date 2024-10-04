package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.FlowerType
import com.example.wrinklethinkle.model.PlayerCharacter

class GrowViewModel(private val playerCharacter: PlayerCharacter) : ViewModel() {

    // Click count for the current flower growth
    private val _clickCount = MutableLiveData<Int>().apply { value = 0 }
    val clickCount: LiveData<Int> get() = _clickCount

    // Alpha value for transparency (if you use it)
    private val _currentAlpha = MutableLiveData<Int>().apply { value = 10 }
    val currentAlpha: LiveData<Int> get() = _currentAlpha

    // Completion status (true when fully grown)
    private val _isComplete = MutableLiveData<Boolean>().apply { value = false }
    val isComplete: LiveData<Boolean> get() = _isComplete

    // Selected flower type
    var selectedFlowerType: FlowerType? = null
        private set

    // Growth stage tracking (0 to 4 stages)
    private val _growthStage = MutableLiveData<Int>().apply { value = 0 }
    val growthStage: LiveData<Int> get() = _growthStage

    // ClickPower multiplier from PlayerCharacter
    var clickPower: Int
        get() = playerCharacter.clickPower.toInt() // Make sure it's in the same format
        private set(value) {
            playerCharacter.clickPower = value.toDouble()
        }

    // Set the selected flower type
    fun setSelectedFlowerType(flowerType: FlowerType) {
        // Ensure the player has this flower available in their inventory
        if ((playerCharacter.flowers[flowerType.name] ?: 0) > 0) {
            selectedFlowerType = flowerType
            // Reset click count, growth stage, and completion flag for new flower
            resetGrowthState()
        } else {
            // Handle case where flower is not available (optional)
            // e.g., show a message that the flower is not available
        }
    }

    // Increment click count based on clickPower and handle growth logic
    fun incrementCount() {
        // Calculate new click count based on player's clickPower
        val currentClickCount = (_clickCount.value ?: 0) + clickPower
        _clickCount.value = currentClickCount

        // Check if the flower should grow to the next stage (needs 50 clicks per stage)
        if (currentClickCount >= 50) {
            _clickCount.value = 0 // Reset click count for the next stage
            growFlower()
        }
    }

    // Logic for growing the flower
    private fun growFlower() {
        val currentStage = _growthStage.value ?: 0
        if (currentStage < 4) {
            // Advance to the next stage
            _growthStage.value = currentStage + 1
        } else {
            // Flower is fully grown
            _isComplete.value = true
        }
    }

    // Reset function for growth state
    private fun resetGrowthState() {
        _clickCount.value = 0
        _growthStage.value = 0
        _isComplete.value = false
        _currentAlpha.value = 10
    }

    // Reset function (if needed to reset the whole state)
    fun reset() {
        resetGrowthState()
    }
}