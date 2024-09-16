package com.example.wrinklethinkle.model

// This data class represents a single plant object from the API response.
// It mirrors the JSON structure from the API so Retrofit can parse it automatically.
data class Plant(
    val author: String,
    val bibliography: String,
    val common_name: String?,
    val family: String,
    val family_common_name: String?,
    val genus: String,
    val genus_id: Int,
    val id: Int,
    val image_url: String?,
    val links: Links,
    val rank: String,
    val scientific_name: String,
    val slug: String,
    val status: String,
    val synonyms: List<String>,
    val year: Int
)

// This data class represents the structure for links in the plant object.
data class Links(
    val genus: String,                     // Link to more information about the genus
    val plant: String,                     // Link to the plant's page
    val self: String                       // Link to the plant's species page
)

data class PlantResponse(
    val data: List<Plant>
)
