package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.databinding.FragmentFlowerDetailBinding
import com.example.wrinklethinkle.model.getFlowerDetails
import com.example.wrinklethinkle.viewmodel.PlayerViewModel
import kotlin.random.Random

class FlowerDetailFragment : Fragment() {
    private var flowerDetailFragmentBinding: FragmentFlowerDetailBinding? = null
    private val binding get() = flowerDetailFragmentBinding!!
    private val playerViewModel = PlayerViewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        flowerDetailFragmentBinding = FragmentFlowerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val flower = arguments?.getString("flower") ?: return
        val flowerData = getFlowerDetails(requireContext(), flower)
        var currentImageIndex = 0
        val shrinkGrowAnimation = AnimationUtils.loadAnimation(context, R.anim.shrink_and_grow)
        if (flowerData != null) {
            binding.flowerImage.setImageResource(mapImage(flower)[0])
            binding.flowerScientificName.text = "Scientific Name: \t ${flowerData.scientificName}"
            binding.flowerName.text = flowerData.name
            binding.flowerColors.text = "Colors: \t"
            for (color in flowerData.colors) {
                binding.flowerColors.append(color + ", ")
            }
            binding.flowerOrigin.text = "Origin: \t ${flowerData.origin}"
            binding.flowerSymbolism.text = "Symbolism: \t ${flowerData.symbolism}"
            binding.flowerFamily.text = "Flower Family: \t ${flowerData.family}"
            binding.flowerHeight.text = "Flower Height: \t ${flowerData.averageHeightCm} cm"
            binding.flowerBloomSeason.text = "Bloom Season: \t ${flowerData.bloomSeason}"
            binding.flowerDescription.text = flowerData.description
            binding.flowerImage.setOnClickListener {
                binding.flowerImage.startAnimation(shrinkGrowAnimation)
                if (currentImageIndex != mapImage(flower).size) {
                    binding.flowerImage.setImageResource(mapImage(flower)[currentImageIndex])
                    currentImageIndex++
                } else {
                    currentImageIndex = 0
                }

            }
        } else {
            Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "We could not load the flower data.")

        }
    }

    fun mapImage(flowerName: String): List<Int> {
        return when(flowerName) {
            "ROSE" -> listOf(R.drawable.rose_flower_real, R.drawable.rose_flower_complete)
            "TULIP" -> listOf(R.drawable.tulip_flower_real, R.drawable.tulip_flower_complete)
            "DAHLIA" -> listOf(R.drawable.black_dahlia_real, R.drawable.black_dahlia_flower_complete)
            "LILY" -> listOf(R.drawable.lily_flower_real, R.drawable.lily_flower_complete)
            else -> listOf(R.drawable.error_screen_cat, R.drawable.project_gnome)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        flowerDetailFragmentBinding = null
    }
}