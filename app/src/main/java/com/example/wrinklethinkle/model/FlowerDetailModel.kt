package com.example.wrinklethinkle.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

data class Flower(
    val name: String,
    val scientificName: String,
    val family: String,
    val description: String,
    val origin: String,
    val bloomSeason: String,
    val colors: List<String>,
    val symbolism: String,
    val averageHeightCm: Int
)

// FlowerResponse data class to hold the list of flowers from the JSON
data class FlowerResponse(
    @SerializedName("flowers") val flowers: List<Flower>
)

fun getJsonDataFromAsset(context: Context, fileName: String): String? {
    return try {
        context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getFlowerDetails(context: Context, flowerName: String): Flower? {
    val jsonString = getJsonDataFromAsset(context, "FlowerData.json") ?: return null

    val gson = Gson()
    val flowerType = object : TypeToken<FlowerResponse>() {}.type
    val flowerResponse: FlowerResponse = gson.fromJson(jsonString, flowerType)

    // Log the JSON string to verify it's being read correctly
    println("JSON String: $jsonString")

    // Log the parsed flowers to verify data
    println("Parsed flowers: ${flowerResponse.flowers}")

    // Find and return the flower with the matching name
    return flowerResponse.flowers.find { it.name.equals(flowerName, ignoreCase = true) }
}
