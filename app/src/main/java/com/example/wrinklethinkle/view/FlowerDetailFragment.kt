package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.databinding.FragmentFlowerDetailBinding
import com.example.wrinklethinkle.model.getFlowerDetails

class FlowerDetailFragment : Fragment() {
    private var flowerDetailFragmentBinding: FragmentFlowerDetailBinding? = null
    private val binding get() = flowerDetailFragmentBinding!!

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
        if (flowerData != null) {
            binding.flowerImage.setImageResource(mapImage(flower))
            binding.flowerName.text = flowerData.name
            for (color in flowerData.colors) {
                binding.flowerColors.append(color + "\n")
            }
            binding.flowerSymbolism.text = flowerData.symbolism
            binding.flowerFamily.text = flowerData.family
            binding.flowerHeight.text = flowerData.averageHeightCm.toString()
            binding.flowerBloomSeason.text = flowerData.bloomSeason
            binding.flowerDescription.text = flowerData.description
        } else {
            Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "We could not load the flower data.")

        }
    }

    fun mapImage(flowerName: String): Int {
        return when(flowerName) {
            "ROSE" -> R.drawable.real_rose
            else -> R.drawable.error_screen_cat
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        flowerDetailFragmentBinding = null
    }
}