package com.example.wrinklethinkle.model

enum class FlowerType(
    val rank: Int,        // Rank of the flower
    val location: String, // Where it can be grown (Garden/Greenhouse)
    val seedCost: Int,    // Cost of the seed in gold
    val clickMultiplier: Int, // Multiplier for clicks needed per stage
    val sellPrice: Int    // Price for selling the completed flower
) {
    ROSE(1, "GARDEN", 50, 25, 100),       // Rank 1: Common
    TULIP(2, "GARDEN", 75, 50, 150),      // Rank 2: Uncommon
    LILY(3, "GREENHOUSE", 100, 75, 200),  // Rank 3: Rare
    DAHLIA(4, "GREENHOUSE", 150, 100, 300) // Rank 4: Legendary
}