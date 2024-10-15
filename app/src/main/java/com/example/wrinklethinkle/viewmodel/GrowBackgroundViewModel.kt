package com.example.wrinklethinkle.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.FlowerType

class GrowBackgroundViewModel : ViewModel() {

    private val _backgroundImage = MutableLiveData<Int>()
    val backgroundImage: LiveData<Int> get() = _backgroundImage

    private val _availableFlowers = MutableLiveData<List<FlowerType>>()
    val availableFlowers: LiveData<List<FlowerType>> get() = _availableFlowers

    fun setBackgroundImage(resourceId: Int) {
        _backgroundImage.value = resourceId
    }

    fun setAvailableFlowers(flowers: List<FlowerType>) {
        _availableFlowers.value = flowers
    }
}
