package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.FlowerType

class GrowBackgroundViewModel : ViewModel() {
    // Background image for the grow screen
    private val _backgroundImage = MutableLiveData<Int>()
    val backgroundImage: LiveData<Int> get() = _backgroundImage

    // Available flowers based on the selected environment
    private val _availableFlowers = MutableLiveData<List<FlowerType>>()
    val availableFlowers: LiveData<List<FlowerType>> get() = _availableFlowers

    // Track the currently active toggle button
    private val _activeButtonId = MutableLiveData<Int?>()
    val activeButtonId: LiveData<Int?> get() = _activeButtonId

    // Set background image and available flowers
    fun setBackgroundImage(imageRes: Int) {
        _backgroundImage.value = imageRes
    }

    fun setAvailableFlowers(flowers: List<FlowerType>) {
        _availableFlowers.value = flowers
    }

    // Set the currently active button ID
    fun setActiveButton(buttonId: Int?) {
        _activeButtonId.value = buttonId
    }
}
