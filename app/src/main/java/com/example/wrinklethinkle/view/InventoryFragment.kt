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

    private val playerViewModel: PlayerViewModel by activityViewModels()
    private var inventoryFragmentBinding: FragmentInventoryBinding? = null
    private val binding get() = inventoryFragmentBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inventoryFragmentBinding = FragmentInventoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerViewModel.playerData.observe(viewLifecycleOwner) { player ->

            val roseSeeds = player.seeds.getOrDefault("ROSE", 0)
            val tulipSeeds = player.seeds.getOrDefault("TULIP", 0)
            val lilySeeds = player.seeds.getOrDefault("LILY", 0)
            val dahliaSeeds = player.seeds.getOrDefault("DAHLIA", 0)

            binding.invCoinBalance.text = player.gold.toString() ?: "0"
            binding.pesticideBalance.text = player.pesticide.toString() ?: "0"
            binding.fertilizerBalance.text = player.fertilizer.toString() ?: "0"
            binding.roseSeedsBalance.text = roseSeeds.toString()
            binding.tulipSeedsBalance.text = tulipSeeds.toString()
            binding.lilySeedsBalance.text = lilySeeds.toString()
            binding.dahliaSeedsBalance.text = dahliaSeeds.toString()

            binding.InventoryShopButton.setOnClickListener {
                findNavController().navigate(R.id.action_InventoryFragment_to_ShopFragment)
            }
            binding.InventoryGrowButton.setOnClickListener {
                findNavController().navigate(R.id.action_InventoryFragment_to_GrowFragment)
            }
            binding.InventoryInventoryButton.setOnClickListener {
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You're already here :)")
            }
            binding.InventoryMapButton.setOnClickListener {
                findNavController().navigate(R.id.action_InventoryFragment_to_MapFragment)
            }
            binding.homeButton.setOnClickListener {
                findNavController().navigate(R.id.action_InventoryFragment_to_InsideHouseFragment)
            }
        }

    }
    override fun onResume() {
        super.onResume()
        playerViewModel.fetchLatestPlayerData()
    }

}