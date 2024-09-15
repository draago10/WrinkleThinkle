package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.Flower
import kotlin.math.min

class GrowViewModel : ViewModel() {

    private val _clickCount = MutableLiveData<Int>().apply { value = 0 }
    val clickCount: LiveData<Int> get() = _clickCount

    private val _currentAlpha = MutableLiveData<Int>().apply { value = 10 }
    val currentAlpha: LiveData<Int> get() = _currentAlpha

    // Maximum alpha value - Temp
    private val maxAlpha = 255
    // Alpha increment per 25 clicks
    private val alphaIncrement = 25
    // Number of clicks to increase alpha
    private val clicksPerIncrement = 5

    private val _isComplete = MutableLiveData<Boolean>().apply { value = false }
    val isComplete: LiveData<Boolean> get() = _isComplete

    private var selectedFlower: Flower? = null

    fun selectedFlower(flower: Flower) {
        selectedFlower = flower
        // Reset click count for each new flower
        _clickCount.value = 0
        // Ensure completion is reset
        _isComplete.value = false
        // Reset alpha
        _currentAlpha.value = 10
    }

    fun incrementCount() {
        val newCount = (_clickCount.value ?: 0) + 1

        // Update internal _clickCount state
        _clickCount.value = newCount

        // Update alpha if it's a multiple of clicksPerIncrement. Alpha will be changed, used for testing.
        if (newCount % clicksPerIncrement == 0) {
            val newAlpha = min(maxAlpha, (_currentAlpha.value ?: 10) + alphaIncrement)
            _currentAlpha.value = newAlpha
        }

        // If the count reaches 25, mark as complete. This will be changed to the actual value later. 25 is for testing
        if (newCount == 25) {
            _isComplete.value = true
        }
    }

    fun reset() {
        // Reset click count
        _clickCount.value = 0
        // Reset alpha
        _currentAlpha.value = 10
        // Reset completion flag
        _isComplete.value = false
    }
}