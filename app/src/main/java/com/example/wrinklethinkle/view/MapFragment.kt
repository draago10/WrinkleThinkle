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
        // Inflate the layout using ViewBinding
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // No need to inflate the layout again, we can directly use binding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up buttons for toggling backgrounds and available flowers
        binding.GardenGrow.setOnClickListener {
            // Set garden background and available flowers
            growBackgroundViewModel.setBackgroundImage(R.drawable.grow_bg_garden)
            growBackgroundViewModel.setAvailableFlowers(
                listOf(FlowerType.ROSE, FlowerType.TULIP) // Available garden flowers
            )
        }

        binding.GreenhouseGrow.setOnClickListener {
            // Set greenhouse background and available flowers
            growBackgroundViewModel.setBackgroundImage(R.drawable.grow_bg_greenhouse)
            growBackgroundViewModel.setAvailableFlowers(
                listOf(FlowerType.LILY, FlowerType.DAHLIA) // Available greenhouse flowers
            )
        }

        // Set up navigation buttons
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
            // Display message or keep it as-is since we're already on the map
            Toast.makeText(requireContext(), "Already on Map Screen", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear the binding reference to avoid memory leaks
    }
}
