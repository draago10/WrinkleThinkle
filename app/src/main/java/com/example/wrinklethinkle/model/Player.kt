package com.example.wrinklethinkle.model

class Player {
    var level: Int = 1              // Player starts at level 1
    var gold: Int = 0               // Player starts with 0 gold
    var clickPower: Double = 1.0    // Base click power, increases with level
    var experience: Int = 0         // Player's current experience
    val inventory = Inventory()     // Player's inventory

    // Calculate experience required for next level
    fun expToNextLevel(): Int {
        return 100 * level
    }

    // Gain experience and level up if necessary
    fun gainExperience(exp: Int) {
        experience += exp
        if (experience >= expToNextLevel()) {
            levelUp()
        }
    }

    // Level up the player
    private fun levelUp() {
        level++
        experience = 0
        clickPower += 0.1
    }

    // Sell a flower and earn gold
    fun sellFlower(flower: Flower) {
        val goldEarned = inventory.sellFlower(flower)
        gold += goldEarned
    }
}