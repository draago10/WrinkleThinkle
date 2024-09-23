package com.example.wrinklethinkle.model

import com.example.wrinklethinkle.R

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

/*
class Flower(
    val type: FlowerType,     // The type of the flower (Rose, Tulip, etc.)
    var growthStage: Int = 1, // Current growth stage of the flower (1 to 4)
    var clicksNeeded: Int     // Number of clicks needed to grow to the next stage
){
    fun grow(clicks: Int) {
        clicksNeeded -= clicks
        if (clicksNeeded <= 0 && growthStage < 4) {
            growthStage++
            clicksNeeded = calculateClicksForNextStage()
        }
    }

    private fun calculateClicksForNextStage(): Int {
        return when (type) {
            FlowerType.ROSE -> 25 * growthStage
            FlowerType.TULIP -> 50 * growthStage
            FlowerType.LILY -> 75 * growthStage
            FlowerType.DAHLIA -> 100 * growthStage
        }
    }
}
 */