package com.example.wrinklethinkle.model

enum class FlowerType(val rank: Int, val location: String) {
    ROSE(1, "GARDEN"),         // Rank 1 flower in the garden
    TULIP(2, "GARDEN"),        // Rank 2 flower in the garden
    LILY(3, "GREENHOUSE"),     // Rank 3 flower in the greenhouse
    DAHLIA(4, "GREENHOUSE")  // Rank 4 flower in the greenhouse
}