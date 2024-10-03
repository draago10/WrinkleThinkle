package com.example.wrinklethinkle.model

import Player

class Flower(
    val type: FlowerType,     // The type of the flower (Rose, Tulip, etc.)
    var growthStage: Int = 1, // Current growth stage of the flower (1 to 4)
    var clicksNeeded: Int = type.clickMultiplier * growthStage // Initial clicks to grow
) {
    companion object {
        const val MAX_GROWTH_STAGE = 4 // Maximum growth stage
    }

    // Function to take in clicks and apply to the clicksNeeded variable
    fun grow(clicks: Int, player: Player) {
        clicksNeeded -= clicks
        if (clicksNeeded <= 0 && growthStage < MAX_GROWTH_STAGE) {
            growthStage++
            clicksNeeded = calculateClicksForNextStage()
        }

        // When flower reaches max growth stage
        if (growthStage == MAX_GROWTH_STAGE) {
            val xpGain = type.rank * 50
            player.gainExperience(xpGain)

            // Add the fully grown flower to the player's inventory
            //player.inventory.addCompletedFlower(this)
        }
    }

    // Function to calculate the clicks needed for the next growth stage
    private fun calculateClicksForNextStage(): Int {
        return type.clickMultiplier * growthStage
    }
}
