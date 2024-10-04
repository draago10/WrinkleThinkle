package com.example.wrinklethinkle.model

class PlayerCharacter (
    var name: String,          // Player's name
    var level: Int = 1,        // Player's level
    var experience: Int = 0,   // Player's experience points
    var gold: Int = 0,         // Player's gold currency
    var clickPower: Double = 1.0, // Player's click power (damage, growth, etc.)
    var pesticide: Int = 0,    // Amount of pesticide available
    var fertilizer: Int = 0,   // Amount of fertilizer available
    var flowers: MutableMap<String, Int> = mutableMapOf(),  // Flower inventory (name -> amount)
    var seeds: MutableMap<String, Int> = mutableMapOf()     // Seed inventory (name -> amount)
) {
            // Function to add experience points and level up if possible ----------------------------------------------
    fun gainExperience(exp: Int) {
        experience += exp
        if (experience >= level*100) {
            val leftover : Int = experience - (level*100)
            level++
            clickPower += 0.1
            experience = 0 + leftover
        }
    }

            // Functions to add/remove flowers ------------------------------------------------------------------------
    fun addFlower(flowerName: String, amount: Int) {
        flowers[flowerName] = flowers.getOrDefault(flowerName, 0) + amount
    }

    fun removeFlower(flowerName: String, amount : Int) {
        val currentAmount = flowers.getOrDefault(flowerName, 0)
        if (currentAmount >= amount) {
            flowers[flowerName] = currentAmount - amount
        }
    }

            // Functions to add/remove seeds --------------------------------------------------------------------------
    fun addSeed(seedName: String, amount: Int) {
        seeds[seedName] = seeds.getOrDefault(seedName, 0) + amount
    }

    fun removeSeed(seedName: String, amount: Int) {
        val currentAmount = seeds.getOrDefault(seedName, 0)
        if (currentAmount >= amount) {
            seeds[seedName] = currentAmount - amount
        }
    }

            // Functions to add/remove gold ---------------------------------------------------------------------------
    fun addGold(goldName: String, amount: Int) {
        gold += amount
    }

    fun removeGold(goldName: String, amount : Int) {
        val currentAmount = gold
        if (currentAmount >= amount) {
            gold -= amount
        }
    }

            // Functions to add/remove pesticide ----------------------------------------------------------------------
    fun addPesticide(pesticideName: String, amount: Int) {
        pesticide += amount
    }

    fun removePesticide(pesticideName: String, amount: Int) {
        val currentAmount = pesticide
        if (currentAmount >= amount) {
            pesticide -= amount
        }
    }

            // Functions to add/remove fertilizer ---------------------------------------------------------------------
    fun addFertilizer(fertilizerName: String, amount: Int) {
        fertilizer += amount
    }

    fun removeFertilizer(fertilizerName: String, amount: Int) {
        val currentAmount = fertilizer
        if (currentAmount >= amount) {
            fertilizer -= amount
        }
    }
}