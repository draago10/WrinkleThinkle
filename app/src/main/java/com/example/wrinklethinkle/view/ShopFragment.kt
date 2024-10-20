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
    private var shopFragmentBinding: FragmentShopBinding? = null
    private val binding get() = shopFragmentBinding!!
    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        shopFragmentBinding = FragmentShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shop = Shop
        playerViewModel.playerData.observe(viewLifecycleOwner) { player ->

            binding.coinBalance.text = player.gold.toString()

            binding.growIcon.setOnClickListener {
                findNavController().navigate(R.id.action_ShopFragment_to_GrowFragment)
            }
            binding.iconInventory.setOnClickListener {
                findNavController().navigate(R.id.action_ShopFragment_to_InventoryFragment)
            }
            binding.shopIcon.setOnClickListener {
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You're already here :)")
            }
            binding.iconMap.setOnClickListener {
                findNavController().navigate(R.id.action_ShopFragment_to_MapFragment)
            }
            binding.homeButton.setOnClickListener {
                findNavController().navigate(R.id.action_ShopFragment_to_InsideHouseFragment)
            }
            binding.purchaseFertilizer.setOnClickListener {

                if (player.gold >= 10)
                {
                    shop.buyFertilizer(player, 10)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Fertilizer purchased!", "Fertilizer has been successfully added to your inventory.")
                }
                else {
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You don't have enough gold.")
                }
            }
            binding.purchasePesticide.setOnClickListener {

                if (player.gold >= 30)
                {
                    shop.buyPesticide(player, 30)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Pesticide purchased!", "Pesticide has been successfully added to your inventory.")
                }
                else {
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You don't have enough gold.")
                }
            }
            binding.purchaseRose.setOnClickListener {
                val bundle = Bundle().apply {
                    putString("flower", FlowerType.ROSE.name)
                }
                //findNavController().navigate(R.id.FlowerDetailFragment, bundle)
                if (player.gold >= FlowerType.ROSE.cost)
                {
                    shop.buySeed(player, FlowerType.ROSE)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Rose seed purchased!", "Rose seed has been successfully added to your inventory.")
                }
                else {
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You don't have enough gold.")
                }
            }
            binding.purchaseTulip.setOnClickListener {
                if (player.gold >= FlowerType.TULIP.cost)
                {
                    shop.buySeed(player, FlowerType.TULIP)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Tulip seed purchased!", "Tulip seed has been successfully added to your inventory.")
                }
                else {
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You don't have enough gold.")
                }
            }
            binding.purchaseLily.setOnClickListener {
                if (player.gold >= FlowerType.LILY.cost)
                {
                    shop.buySeed(player, FlowerType.LILY)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Lily seed purchased!", "Lily seed has been successfully added to your inventory.")
                }
                else {
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You don't have enough gold.")
                }
            }
            binding.purchaseDahlia.setOnClickListener {
                if (player.gold >= FlowerType.DAHLIA.cost)
                {
                    shop.buySeed(player, FlowerType.DAHLIA)
                    binding.coinBalance.text = player.gold.toString()
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Dahlia seed purchased!", "Dahlia seed has been successfully added to your inventory.")
                }
                else {
                    Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You don't have enough gold.")
                }
            }
            binding.sellFlowers.setOnClickListener {

                val dialog = AlertDialog.Builder(requireContext())
                dialog.setTitle("Choose a flower to sell")

                if (player.flowers.isEmpty())
                {
                    dialog.setMessage("No flowers currently in inventory.")
                    dialog.setPositiveButton(android.R.string.ok, null)
                    dialog.show()

                    return@setOnClickListener
                }
                val flowers = player.flowers.keys.toTypedArray()
                dialog.setItems(flowers) { _ , which ->
                    val selectedFlower = flowers[which]
                    for ((key, _ ) in player.flowers)  {
                        if (selectedFlower == "ROSE")
                        {
                            player.removeFlower(selectedFlower,1 , 100)
                            break
                        }
                        if (selectedFlower == "TULIP")
                        {
                            player.removeFlower(key, 1, 250)
                            break
                        }
                        if (selectedFlower == "LILY")
                        {
                            player.removeFlower(key, 1, 500)
                            break
                        }
                        if (selectedFlower == "DAHLIA")
                        {
                            player.removeFlower(key, 1, 1000)
                            break
                        }
                    }
                    binding.coinBalance.text = player.gold.toString()
                }
                dialog.show()
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        shopFragmentBinding = null
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.fetchLatestPlayerData()
    }
}