package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.databinding.FragmentShopBinding
import com.example.wrinklethinkle.model.FlowerType
import com.example.wrinklethinkle.model.Shop
import com.example.wrinklethinkle.viewmodel.PlayerViewModel
import androidx.appcompat.app.AlertDialog

class ShopFragment : Fragment() {
    // Nullable binding object for the fragment's view binding
    private var shopFragmentBinding: FragmentShopBinding? = null
    // Getter to safely access the binding once it's initialized
    private val binding get() = shopFragmentBinding!!
    // ViewModel to access player-related data shared between activities/fragments
    private val playerViewModel: PlayerViewModel by activityViewModels()

    // Called when the fragment is created, but no initialization logic is needed here for now
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Called to inflate the layout and initialize the fragment's view binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize the view binding for the ShopFragment layout
        shopFragmentBinding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called after the view has been created to set up UI interactions and observers
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reference to the Shop object (assuming it's a singleton or globally accessible)
        val shop = Shop

        // Observe changes to player data and update the UI accordingly
        playerViewModel.playerData.observe(viewLifecycleOwner) { player ->
            // Update the coin balance text with the player's current gold
            binding.coinBalance.text = player.gold.toString()

            // Navigate to other fragments when respective buttons are clicked
            binding.growIcon.setOnClickListener {
                findNavController().navigate(R.id.action_ShopFragment_to_GrowFragment)
            }
            binding.iconInventory.setOnClickListener {
                findNavController().navigate(R.id.action_ShopFragment_to_InventoryFragment)
            }
            binding.shopIcon.setOnClickListener {
                // Show a popup indicating the user is already in the shop
                Utility().showErrorPopup(
                    childFragmentManager,
                    requireContext(),
                    R.drawable.project_gnome,
                    "Oh my gnome!",
                    "You're already here :)"
                )
            }
            binding.iconMap.setOnClickListener {
                findNavController().navigate(R.id.action_ShopFragment_to_MapFragment)
            }
            binding.homeButton.setOnClickListener {
                findNavController().navigate(R.id.action_ShopFragment_to_InsideHouseFragment)
            }

            // Purchase fertilizer if the player has enough gold
            binding.purchaseFertilizer.setOnClickListener {
                if (player.gold >= 10) {
                    shop.buyFertilizer(player, 10)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Fertilizer purchased!",
                        "Fertilizer has been successfully added to your inventory."
                    )
                } else {
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Oh my gnome!",
                        "You don't have enough gold."
                    )
                }
            }

            // Purchase pesticide if the player has enough gold
            binding.purchasePesticide.setOnClickListener {
                if (player.gold >= 30) {
                    shop.buyPesticide(player, 30)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Pesticide purchased!",
                        "Pesticide has been successfully added to your inventory."
                    )
                } else {
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Oh my gnome!",
                        "You don't have enough gold."
                    )
                }
            }

            // Purchase a rose seed if the player has enough gold
            binding.purchaseRose.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("flower", FlowerType.ROSE.name)
                }
                if (player.gold >= FlowerType.ROSE.cost) {
                    shop.buySeed(player, FlowerType.ROSE)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Rose seed purchased!",
                        "Rose seed has been successfully added to your inventory."
                    )
                } else {
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Oh my gnome!",
                        "You don't have enough gold."
                    )
                }
            }

            // Similar code for purchasing tulip, lily, and dahlia seeds
            binding.purchaseTulip.setOnClickListener {
                if (player.gold >= FlowerType.TULIP.cost) {
                    shop.buySeed(player, FlowerType.TULIP)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Tulip seed purchased!",
                        "Tulip seed has been successfully added to your inventory."
                    )
                } else {
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Oh my gnome!",
                        "You don't have enough gold."
                    )
                }
            }

            binding.purchaseLily.setOnClickListener {
                if (player.gold >= FlowerType.LILY.cost) {
                    shop.buySeed(player, FlowerType.LILY)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Lily seed purchased!",
                        "Lily seed has been successfully added to your inventory."
                    )
                } else {
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Oh my gnome!",
                        "You don't have enough gold."
                    )
                }
            }

            binding.purchaseDahlia.setOnClickListener {
                if (player.gold >= FlowerType.DAHLIA.cost) {
                    shop.buySeed(player, FlowerType.DAHLIA)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Dahlia seed purchased!",
                        "Dahlia seed has been successfully added to your inventory."
                    )
                } else {
                    Utility().showErrorPopup(
                        childFragmentManager,
                        requireContext(),
                        R.drawable.project_gnome,
                        "Oh my gnome!",
                        "You don't have enough gold."
                    )
                }
            }

            // Selling flowers dialog with different options
            binding.sellFlowers.setOnClickListener {
                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Choose a flower to sell")

                if (player.flowers.isEmpty()) {
                    dialog.setMessage("No flowers currently in inventory.")
                    dialog.setPositiveButton(android.R.string.ok, null)
                    dialog.show()

                    return@setOnClickListener
                }

                // Show a list of flowers the player can sell
                val flowers = player.flowers.keys.toTypedArray()
                dialog.setItems(flowers) { _, which ->
                    val selectedFlower = flowers[which]
                    for ((key, _) in player.flowers) {
                        when (selectedFlower) {
                            "ROSE" -> player.removeFlower(selectedFlower, 1, 100)
                            "TULIP" -> player.removeFlower(key, 1, 250)
                            "LILY" -> player.removeFlower(key, 1, 500)
                            "DAHLIA" -> player.removeFlower(key, 1, 1000)
                        }
                    }
                    binding.coinBalance.text = player.gold.toString()
                }
                dialog.show()
            }
        }
    }

    // Cleanup to prevent memory leaks by setting binding to null
    override fun onDestroy() {
        super.onDestroy()
        shopFragmentBinding = null
    }

    // Called when fragment resumes to fetch the latest player data
    override fun onResume() {
        super.onResume()
        playerViewModel.fetchLatestPlayerData()
    }
}
