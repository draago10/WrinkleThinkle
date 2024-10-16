package com.example.wrinklethinkle.viewmodel

import com.example.wrinklethinkle.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.FlowerType

class GrowBackgroundViewModel : ViewModel() {
    private val _backgroundImage = MutableLiveData<Int>().apply {
        value = R.drawable.grow_bg_garden // Default to Garden background
    }
    val backgroundImage: LiveData<Int> = _backgroundImage

    private val _availableFlowers = MutableLiveData<List<FlowerType>>().apply {
        value = listOf(FlowerType.ROSE, FlowerType.TULIP) // Default to Garden flowers
    }
    val availableFlowers: LiveData<List<FlowerType>> = _availableFlowers

    private val _activeButtonId = MutableLiveData<Int?>().apply {
        value = R.id.GardenGrow // Default to Garden button being active
    }
    val activeButtonId: LiveData<Int?> = _activeButtonId

    fun setBackgroundImage(image: Int) {
        _backgroundImage.value = image
    }

    fun setAvailableFlowers(flowers: List<FlowerType>) {
        _availableFlowers.value = flowers
    }

    fun setActiveButton(buttonId: Int?) {
        _activeButtonId.value = buttonId
    }
}
