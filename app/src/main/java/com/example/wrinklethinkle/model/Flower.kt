package com.example.wrinklethinkle.model

import com.example.wrinklethinkle.R
/*
open class Flower(
    val name: String,
    val image: Int,
    var pieces: List<Int>? = null,
    var salePrice: Int? = null,
    var tapCount: Int = 0,    // Default tapCount initialized to 0
    var location: String? = null
)

object BlackDahlia : Flower(
    name = "Black Dahlia",
    image = R.drawable.black_dahlia_flower_complete,
    pieces = listOf(R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_budding_two, R.drawable.black_dahlia_flower_complete),
    tapCount = 0,
    location = "Garden"
)
*/


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
            player.inventory.addCompletedFlower(this)
        }
    }

    // Function to calculate the clicks needed for the next growth stage
    private fun calculateClicksForNextStage(): Int {
        return type.clickMultiplier * growthStage
    }

    // Function to get the sell price of a fully grown flower
    fun getSellPrice(): Int {
        return if (growthStage == MAX_GROWTH_STAGE) type.sellPrice else 0
    }
}
