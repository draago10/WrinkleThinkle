package com.example.wrinklethinkle.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wrinklethinkle.model.Plant
import com.example.wrinklethinkle.model.PlantResponse
import com.example.wrinklethinkle.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel for handling plant data. It makes the API call and stores the result in LiveData.
class FlowerViewModel : ViewModel() {

    // MutableLiveData holds the list of plants fetched from the API. It can be updated internally.
    private val _flowers = MutableLiveData<List<Plant>>()
    // The public LiveData can be observed by the UI, but it cannot be modified externally.
    val flowers: LiveData<List<Plant>> get() = _flowers

    // MutableLiveData to store error messages, if any, during the API call.
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // This function makes the API call to search for plants. The API token and search query are passed as parameters.
    fun searchFlowers(token: String, commonName: String) {
        RetrofitInstance.api.searchPlants(token, commonName).enqueue(object : Callback<PlantResponse> {
            override fun onResponse(call: Call<PlantResponse>, response: Response<PlantResponse>) {
                if (response.isSuccessful) {
                    _flowers.value = response.body()?.data ?: emptyList()
                } else {
                    _error.value = "Error: ${response.code()} - ${response.errorBody()?.string()}"
                }
            }

            override fun onFailure(call: Call<PlantResponse>, t: Throwable) {
                _error.value = t.message
            }
        })
    }
}
