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
}