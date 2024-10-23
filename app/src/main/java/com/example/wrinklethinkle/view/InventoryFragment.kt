package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.databinding.FragmentInventoryBinding
import com.example.wrinklethinkle.databinding.FragmentShopBinding
import com.example.wrinklethinkle.model.FlowerType
import com.google.firebase.database.DatabaseReference
import com.example.wrinklethinkle.viewmodel.PlayerViewModel

class InventoryFragment : Fragment() {

    private val playerViewModel: PlayerViewModel by activityViewModels() // ViewModel to access player data
    private var inventoryFragmentBinding: FragmentInventoryBinding? = null // Binding for the fragment's views
    private val binding get() = inventoryFragmentBinding!! // Non-nullable access to binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Call to the superclass method
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        inventoryFragmentBinding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root // Return the root view of the binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) // Call to the superclass method

        // Observe player data to update the UI with inventory details
        playerViewModel.playerData.observe(viewLifecycleOwner) { player ->

            // Retrieve the number of each type of seed from the player's data
            val roseSeeds = player.seeds.getOrDefault("ROSE", 0)
            val tulipSeeds = player.seeds.getOrDefault("TULIP", 0)
            val lilySeeds = player.seeds.getOrDefault("LILY", 0)
            val dahliaSeeds = player.seeds.getOrDefault("DAHLIA", 0)

            // Update the UI with player's current balances and seed counts
            binding.invCoinBalance.text = player.gold.toString() ?: "0" // Display player's gold
            binding.pesticideBalance.text = player.pesticide.toString() ?: "0" // Display pesticide count
            binding.fertilizerBalance.text = player.fertilizer.toString() ?: "0" // Display fertilizer count
            binding.roseSeedsBalance.text = roseSeeds.toString() // Display rose seeds count
            binding.tulipSeedsBalance.text = tulipSeeds.toString() // Display tulip seeds count
            binding.lilySeedsBalance.text = lilySeeds.toString() // Display lily seeds count
            binding.dahliaSeedsBalance.text = dahliaSeeds.toString() // Display dahlia seeds count

            // Set up click listeners for navigation buttons
            binding.InventoryShopButton.setOnClickListener {
                findNavController().navigate(R.id.action_InventoryFragment_to_ShopFragment) // Navigate to ShopFragment
            }
            binding.InventoryGrowButton.setOnClickListener {
                findNavController().navigate(R.id.action_InventoryFragment_to_GrowFragment) // Navigate to GrowFragment
            }
            binding.InventoryInventoryButton.setOnClickListener {
                // Show error popup if trying to access the inventory again
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome, "Oh my gnome!", "You're already here :)")
            }
            binding.InventoryMapButton.setOnClickListener {
                findNavController().navigate(R.id.action_InventoryFragment_to_MapFragment) // Navigate to MapFragment
            }
            binding.homeButton.setOnClickListener {
                findNavController().navigate(R.id.action_InventoryFragment_to_InsideHouseFragment) // Navigate to InsideHouseFragment
            }
        }
    }

    override fun onResume() {
        super.onResume() // Call to the superclass method
        playerViewModel.fetchLatestPlayerData() // Fetch the latest player data when the fragment resumes
    }
}
