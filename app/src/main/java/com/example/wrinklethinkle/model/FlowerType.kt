package com.example.wrinklethinkle.model

import com.example.wrinklethinkle.R

enum class FlowerType(
    val rank: Int,        // Rank of the flower
    val location: String, // Where it can be grown (Garden/Greenhouse)
    val seedCost: Int,    // Cost of the seed in gold
    val clickMultiplier: Int, // Multiplier for clicks needed per stage
    val sellPrice: Int,    // Price for selling the completed flower
    val image: Int        // Flower image
) {
    ROSE(1, "GARDEN", 50, 25, 100, R.drawable.black_dahlia_flower_complete),       // Rank 1: Common
    TULIP(2, "GARDEN", 75, 50, 150, R.drawable.black_dahlia_flower_complete),      // Rank 2: Uncommon
    LILY(3, "GREENHOUSE", 100, 75, 200, R.drawable.black_dahlia_flower_complete),  // Rank 3: Rare
    DAHLIA(4, "GREENHOUSE", 150, 100, 300, R.drawable.black_dahlia_flower_complete) // Rank 4: Legendary
}