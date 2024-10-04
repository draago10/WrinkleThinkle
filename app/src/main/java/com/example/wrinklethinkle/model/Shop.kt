package com.example.wrinklethinkle.model

object Shop
{
    // Buy seeds for a specific flower type
    fun buySeed(player: Player, type: FlowerType, quantity: Int = 1)
    {
        val seedCost = type.seedCost * quantity
        player.gold -= seedCost
        player.inventory.addSeed(type, quantity)
    }

    // Buy pesticide
    fun buyPesticide(player: Player, price: Int)
    {
        player.gold -= price
        player.inventory.addPesticide(1)
    }

    // Buy fertilizer
    fun buyFertilizer(player: Player, price: Int)
    {
        player.gold -= price
        player.inventory.addFertilizer(1)
    }
}