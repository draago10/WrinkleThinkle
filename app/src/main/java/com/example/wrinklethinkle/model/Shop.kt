package com.example.wrinklethinkle.model

class Shop {
    fun buySeed(player: Player, type: FlowerType, seedCost: Int) {
        if (player.gold >= seedCost) {
            player.gold -= seedCost
            player.inventory.addSeed(type, 1)
        }
    }

    fun buyPesticide(player: Player) {
        val price = 20
        if (player.gold >= price) {
            player.gold -= price
            player.inventory.addPesticide(1)
        }
    }

    fun buyFertilizer(player: Player) {
        val price = 1
        if (player.gold >= price) {
            player.gold -= price
            player.inventory.addFertilizer(1)
        }
    }
}