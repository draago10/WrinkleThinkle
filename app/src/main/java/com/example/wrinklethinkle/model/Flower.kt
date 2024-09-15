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