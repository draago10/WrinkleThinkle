package com.example.wrinklethinkle.model

class Inventory {
    val seeds = mutableMapOf<FlowerType, Int>()   // Seed inventory, mapping flower type to quantity
    var pesticide: Int = 0                        // Amount of pesticide the player owns
    var fertilizer: Int = 0                       // Amount of fertilizer the player owns
    val completedFlowers = mutableListOf<Flower>() // List of completed flowers


    fun addSeed(type: FlowerType, quantity: Int) {
        seeds[type] = seeds.getOrDefault(type, 0) + quantity
    }

    fun addPesticide(quantity: Int) {
        pesticide += quantity
    }

    fun addFertilizer(quantity: Int) {
        fertilizer += quantity
    }

    fun addCompletedFlower(flower: Flower) {
        completedFlowers.add(flower)
    }
}