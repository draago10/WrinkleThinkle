package com.example.wrinklethinkle.model

import com.example.wrinklethinkle.R

enum class FlowerType(val seedImage: Int, val sproutImage: Int, val buddingImage: Int, val flowerImage: Int, val cost: Int) {
    ROSE(R.drawable.seeds_rose, R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_complete, 50),
    TULIP(R.drawable.seeds_tulip, R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_complete, 75),
    LILY(R.drawable.seeds_lily, R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_complete, 100),
    DAHLIA(R.drawable.seeds_dahlia, R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_complete, 150);
}