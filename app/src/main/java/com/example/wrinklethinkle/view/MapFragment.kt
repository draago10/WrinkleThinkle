package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.model.FlowerType
import com.example.wrinklethinkle.viewmodel.GrowBackgroundViewModel
import com.example.wrinklethinkle.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private val growBackgroundViewModel: GrowBackgroundViewModel by activityViewModels()
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore the active button state when the fragment is recreated
        growBackgroundViewModel.activeButtonId.observe(viewLifecycleOwner) { activeButtonId ->
            resetButtonStyles() // Reset both buttons to their inactive state
            if (activeButtonId != null) {
                // Set the correct button to active based on the ViewModel
                when (activeButtonId) {
                    binding.GardenGrow.id -> {
                        binding.GardenGrow.setBackgroundResource(R.drawable.button_grow)
                    }
                    binding.GreenhouseGrow.id -> {
                        binding.GreenhouseGrow.setBackgroundResource(R.drawable.button_grow)
                    }
                }
            }
        }

        // Toggle between GardenGrow and GreenhouseGrow
        binding.GardenGrow.setOnClickListener {
            toggleBackground(R.drawable.grow_bg_garden, listOf(FlowerType.ROSE, FlowerType.TULIP), binding.GardenGrow.id)
        }

        binding.GreenhouseGrow.setOnClickListener {
            toggleBackground(R.drawable.grow_bg_greenhouse, listOf(FlowerType.LILY, FlowerType.DAHLIA), binding.GreenhouseGrow.id)
        }

        // Navigation buttons
        binding.HouseGrowButton.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_GrowFragment)
        }
        binding.HouseInventoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_InventoryFragment)
        }
        binding.HouseShopButton.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_ShopFragment)
        }
        binding.HouseMapButton.setOnClickListener {
            Toast.makeText(requireContext(), "Already on Map Screen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleBackground(backgroundImage: Int, availableFlowers: List<FlowerType>, buttonId: Int) {
        if (growBackgroundViewModel.activeButtonId.value == buttonId) {
            // If the current active button is clicked again, deactivate it
            growBackgroundViewModel.setActiveButton(null)
            growBackgroundViewModel.setBackgroundImage(R.drawable.grow_bg_garden) // Reset to default background
            growBackgroundViewModel.setAvailableFlowers(emptyList()) // No flowers selected
            resetButtonStyles() // Set both buttons to inactive state
        } else {
            // If a different button is clicked, set it as active
            growBackgroundViewModel.setActiveButton(buttonId)
            growBackgroundViewModel.setBackgroundImage(backgroundImage)
            growBackgroundViewModel.setAvailableFlowers(availableFlowers)
            resetButtonStyles() // Reset both buttons before setting the active one

            when (buttonId) {
                binding.GardenGrow.id -> binding.GardenGrow.setBackgroundResource(R.drawable.button_grow)
                binding.GreenhouseGrow.id -> binding.GreenhouseGrow.setBackgroundResource(R.drawable.button_grow)
            }
        }
    }

    private fun resetButtonStyles() {
        binding.GardenGrow.setBackgroundResource(R.drawable.icon_grow)
        binding.GreenhouseGrow.setBackgroundResource(R.drawable.icon_grow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
