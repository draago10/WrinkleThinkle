package com.example.wrinklethinkle.model

import com.example.wrinklethinkle.R

enum class FlowerType(val seedImage: Int, val sproutImage: Int, val buddingImage: Int, val flowerImage: Int) {
    ROSE(R.drawable.seeds_cf_celosia, R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_complete),
    TULIP(R.drawable.seeds_lily, R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_complete),
    LILY(R.drawable.seeds_lily, R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_complete),
    DAHLIA(R.drawable.seeds_black_dahlia, R.drawable.black_dahlia_flower_sprout, R.drawable.black_dahlia_flower_budding, R.drawable.black_dahlia_flower_complete);
}