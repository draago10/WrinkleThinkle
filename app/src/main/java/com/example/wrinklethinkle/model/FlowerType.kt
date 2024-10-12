package com.example.wrinklethinkle.model

import com.example.wrinklethinkle.R

enum class FlowerType(val seedImage: Int,
                      val sproutImage: Int,
                      val buddingImage: Int,
                      val flowerImage: Int,
                      val flowerRank: Int,
                      val cost: Int) {
    ROSE(R.drawable.seeds_rose,
        R.drawable.rose_flower_sprout,
        R.drawable.rose_flower_budding,
        R.drawable.rose_flower_complete,
        1,
        50),
    TULIP(R.drawable.seeds_tulip,
        R.drawable.tulip_flower_sprout,
        R.drawable.tulip_flower_budding,
        R.drawable.tulip_flower_complete,
        2,
        75),
    LILY(R.drawable.seeds_lily,
        R.drawable.lily_flower_sprout,
        R.drawable.lily_flower_budding_two,
        R.drawable.lily_flower_complete,
        3,
        100),
    DAHLIA(R.drawable.seeds_dahlia,
        R.drawable.black_dahlia_flower_sprout,
        R.drawable.black_dahlia_flower_budding,
        R.drawable.black_dahlia_flower_complete,
        4,
        150);
}