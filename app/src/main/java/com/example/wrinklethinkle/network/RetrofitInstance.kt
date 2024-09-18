package com.example.wrinklethinkle.network
import APIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // Base URL for the API. All API calls will use this as the starting point.
    private const val BASE_URL = "https://trefle.io/api/v1/"

    // Lazy initialization of the Retrofit instance, which will only be created when it's first needed.
    val api: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}
