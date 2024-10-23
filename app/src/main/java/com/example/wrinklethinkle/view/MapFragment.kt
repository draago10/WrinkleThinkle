package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.model.FlowerType
import com.example.wrinklethinkle.viewmodel.GrowBackgroundViewModel
import com.example.wrinklethinkle.databinding.FragmentMapBinding
import com.example.wrinklethinkle.viewmodel.PlayerViewModel

class MapFragment : Fragment() {

    private val growBackgroundViewModel: GrowBackgroundViewModel by activityViewModels() // ViewModel for managing the background state
    private val playerViewModel: PlayerViewModel by activityViewModels() // ViewModel for accessing player data
    private var _binding: FragmentMapBinding? = null // Binding for the fragment's views
    private val binding get() = _binding!! // Non-nullable access to binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root // Return the root view of the binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore the active button state when the fragment is recreated
        growBackgroundViewModel.activeButtonId.observe(viewLifecycleOwner) { activeButtonId ->
            resetButtonStyles() // Reset both buttons to their inactive state
            if (activeButtonId != null) {
                // Set the active button's background based on its ID
                when (activeButtonId) {
                    binding.GardenGrow.id -> {
                        binding.GardenGrow.setBackgroundResource(R.drawable.button_grow) // Set GardenGrow as active
                    }
                    binding.GreenhouseGrow.id -> {
                        binding.GreenhouseGrow.setBackgroundResource(R.drawable.button_grow) // Set GreenhouseGrow as active
                    }
                }
            } else {
                // If no button is active, set GardenGrow as the default active button
                binding.GardenGrow.setBackgroundResource(R.drawable.button_grow)
            }
        }

        // Observe player level and enable/disable the GreenhouseGrow button accordingly
        playerViewModel.playerData.observe(viewLifecycleOwner) { playerCharacter ->

            if (playerCharacter.level >= 5) {
                // Enable GreenhouseGrow button if player is level 5 or higher
                binding.GreenhouseGrow.isEnabled = true
                binding.GreenhouseGrow.alpha = 1.0f // Make the button fully visible
            } else {
                // Disable GreenhouseGrow button if player is below level 5
                binding.GreenhouseGrow.isEnabled = false
                binding.GreenhouseGrow.alpha = 0.5f // Make the button semi-transparent
            }
        }

        // Set click listeners for GardenGrow and GreenhouseGrow buttons
        binding.GardenGrow.setOnClickListener {
            // Toggle background and available flowers for GardenGrow
            toggleBackground(R.drawable.grow_bg_garden, listOf(FlowerType.ROSE, FlowerType.TULIP), binding.GardenGrow.id)
        }

        binding.GreenhouseGrow.setOnClickListener {
            // Check if the GreenhouseGrow button is enabled before toggling
            if (binding.GreenhouseGrow.isEnabled) {
                toggleBackground(R.drawable.grow_bg_greenhouse, listOf(FlowerType.LILY, FlowerType.DAHLIA), binding.GreenhouseGrow.id)
            } else {
                // Show a message if player needs to reach level 4 to unlock the Greenhouse
                Toast.makeText(requireContext(), "Reach level 4 to unlock the Greenhouse!", Toast.LENGTH_SHORT).show()
            }
        }

        // Set up navigation buttons for different fragments
        binding.HouseGrowButton.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_GrowFragment) // Navigate to GrowFragment
        }
        binding.HouseInventoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_InventoryFragment) // Navigate to InventoryFragment
        }
        binding.HouseShopButton.setOnClickListener {
            findNavController().navigate(R.id.action_MapFragment_to_ShopFragment) // Navigate to ShopFragment
        }
        binding.HouseMapButton.setOnClickListener {
            // Show an error popup if trying to access the map while already there
            Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome, "Oh my gnome!", "You're already here :)")
        }
    }

    private fun toggleBackground(backgroundImage: Int, availableFlowers: List<FlowerType>, buttonId: Int) {
        if (growBackgroundViewModel.activeButtonId.value == buttonId) {
            // If the current active button is clicked again, deactivate it
            growBackgroundViewModel.setActiveButton(null) // Clear active button
            growBackgroundViewModel.setBackgroundImage(R.drawable.grow_bg_garden) // Reset to default background
            growBackgroundViewModel.setAvailableFlowers(emptyList()) // Clear available flowers
            resetButtonStyles() // Set both buttons to inactive state
        } else {
            // If a different button is clicked, set it as active
            growBackgroundViewModel.setActiveButton(buttonId) // Update active button
            growBackgroundViewModel.setBackgroundImage(backgroundImage) // Set new background
            growBackgroundViewModel.setAvailableFlowers(availableFlowers) // Set new available flowers
            resetButtonStyles() // Reset both buttons before setting the active one

            // Highlight the active button
            when (buttonId) {
                binding.GardenGrow.id -> binding.GardenGrow.setBackgroundResource(R.drawable.button_grow)
                binding.GreenhouseGrow.id -> binding.GreenhouseGrow.setBackgroundResource(R.drawable.button_grow)
            }
        }
    }

    private fun resetButtonStyles() {
        // Reset button styles to inactive
        binding.GardenGrow.setBackgroundResource(R.drawable.icon_grow)
        binding.GreenhouseGrow.setBackgroundResource(R.drawable.icon_grow)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding to avoid memory leaks
    }
}
