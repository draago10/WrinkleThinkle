package com.example.wrinklethinkle.model

class Shop {
    // Buy seeds for a specific flower type
    fun buySeed(player: Player, type: FlowerType, quantity: Int = 1) {
        val seedCost = type.seedCost * quantity
        if (player.gold >= seedCost) {
            player.gold -= seedCost
            player.inventory.addSeed(type, quantity)
        }
    }

    // Buy pesticide
    fun buyPesticide(player: Player) {
        val price = 20
        if (player.gold >= price) {
            player.gold -= price
            player.inventory.addPesticide(1)
        }
    }

    // Buy fertilizer
    fun buyFertilizer(player: Player) {
        val price = 1
        if (player.gold >= price) {
            player.gold -= price
            player.inventory.addFertilizer(1)
        }
    }
}