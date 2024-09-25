package com.example.wrinklethinkle.model

class Inventory {
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

    // Add pesticide
    fun addPesticide(quantity: Int) {
        pesticide += quantity
    }

    // Add fertilizer
    fun addFertilizer(quantity: Int) {
        fertilizer += quantity
    }

    // Add completed flower to inventory
    fun addCompletedFlower(flower: Flower) {
        completedFlowers.add(flower)
    }

//    // Sell a completed flower and remove it from the inventory
//    fun sellFlower(flower: Flower): Int {
//        if (completedFlowers.contains(flower) && flower.growthStage == Flower.MAX_GROWTH_STAGE) {
//            completedFlowers.remove(flower)
//            return flower.getSellPrice() // Return the amount of gold earned from the sale
//        }
//        return 0 // If the flower isn't fully grown or not in inventory, return 0
//    }

}