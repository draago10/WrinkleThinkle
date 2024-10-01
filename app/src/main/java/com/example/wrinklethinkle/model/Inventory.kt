package com.example.wrinklethinkle.model

object Inventory {
    val seeds = mutableMapOf<FlowerType, Int>()   // Seed inventory, mapping flower type to quantity
    var pesticide: Int = 0                        // Amount of pesticide the player owns
    var fertilizer: Int = 0                       // Amount of fertilizer the player owns
    val completedFlowers = mutableListOf<Flower>() // List of completed flowers

    // Add seeds of a specific type
    fun addSeed(type: FlowerType, quantity: Int) {
        seeds[type] = seeds.getOrDefault(type, 0) + quantity
    }

    // Remove seed when planting
    fun removeSeed(type: FlowerType) {
        val currentQuantity = seeds.getOrDefault(type, 0)
        if (currentQuantity > 0) {
            seeds[type] = currentQuantity - 1
        }
    }

    // Add completed flower to inventory
    fun addCompletedFlower(flower: Flower) {
        completedFlowers.add(flower)
    }

    // Get list of available seeds
    fun getAvailableSeeds(): List<FlowerType> {
        return seeds.filter { it.value > 0 }.keys.toList()
    }
}