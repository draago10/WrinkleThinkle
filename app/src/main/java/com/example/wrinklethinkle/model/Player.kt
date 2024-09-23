package com.example.wrinklethinkle.model

class Player {
    var level: Int = 1              // Player starts at level 1
    var gold: Int = 0               // Player starts with 0 gold
    var clickPower: Double = 1.0    // Base click power, increases with level
    var experience: Int = 0         // Player's current experience
    val inventory = Inventory()     // Player's inventory
    var bugCount: Int = 0           // Current number of bugs affecting the player
    private val maxBugs = 10        // Maximum number of bugs that can spawn

    fun expToNextLevel(): Int {
        return 100 * level
    }

    fun gainExperience(exp: Int) {
        experience += exp
        if (experience >= expToNextLevel()) {
            levelUp()
        }
    }

    private fun levelUp() {
        level++
        experience = 0
        clickPower += 0.1
    }

    fun applyClick() {
        if (bugCount > 0) {
            clickPower = maxOf(clickPower - (0.1 * bugCount), 0.0)
        }
    }

    fun spawnBug(clicks: Int) {
        if (clicks % 5 == 0 && Math.random() <= 0.20 && bugCount < maxBugs) {
            bugCount++
        }
    }

    fun usePesticide() {
        if (inventory.pesticide > 0 && bugCount > 0) {
            inventory.pesticide--
            bugCount = maxOf(bugCount - 5, 0)
            clickPower = 1.0 + (level * 0.1)
        }
    }
}