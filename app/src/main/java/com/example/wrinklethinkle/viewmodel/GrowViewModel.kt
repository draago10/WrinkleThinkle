package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.min

class GrowViewModel : ViewModel() {

    private val _clickCount = MutableLiveData<Int>().apply { value = 0 }
    val clickCount: LiveData<Int> get() = _clickCount

    private val _currentAlpha = MutableLiveData<Int>().apply { value = 10 }
    val currentAlpha: LiveData<Int> get() = _currentAlpha

    private val maxAlpha = 255            // Maximum alpha value
    private val alphaIncrement = 25       // Alpha increment per 25 clicks
    private val clicksPerIncrement = 25   // Number of clicks to increase alpha

    private val _isComplete = MutableLiveData<Boolean>().apply { value = false }
    val isComplete: LiveData<Boolean> get() = _isComplete

    fun incrementCount() {
        val currentCount = _clickCount.value ?: 0
        val newCount = currentCount + 1
        _clickCount.value = newCount

        // Update alpha if it's a multiple of clicksPerIncrement
        if (newCount % clicksPerIncrement == 0) {
            val newAlpha = min(maxAlpha, (_currentAlpha.value ?: 10) + alphaIncrement)
            _currentAlpha.value = newAlpha
        }

        // If the count reaches maxAlpha, mark as complete
        if (newCount == maxAlpha) {
            _isComplete.value = true
        }
    }
}