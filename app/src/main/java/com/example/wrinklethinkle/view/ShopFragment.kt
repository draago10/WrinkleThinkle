package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.databinding.FragmentInsideHouseBinding
import com.example.wrinklethinkle.databinding.FragmentShopBinding
import com.example.wrinklethinkle.model.FlowerType
import com.example.wrinklethinkle.model.Player
import com.example.wrinklethinkle.model.Shop

class ShopFragment : Fragment() {
    private var shopFragmentBinding: FragmentShopBinding? = null
    private val binding get() = shopFragmentBinding!!

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

        var player = Player
        var shop = Shop

        player.inventory.seeds.put(FlowerType.ROSE, 0)
        player.inventory.seeds.put(FlowerType.TULIP, 0)
        player.inventory.seeds.put(FlowerType.LILY, 0)
        player.inventory.seeds.put(FlowerType.DAHLIA, 0)

        binding.coinBalance.text = player.gold.toString()

        binding.growIcon.setOnClickListener {
            findNavController().navigate(R.id.action_ShopFragment_to_GrowFragment)
        }
        binding.iconInventory.setOnClickListener {
            findNavController().navigate(R.id.action_ShopFragment_to_InventoryFragment)
        }
        binding.shopIcon.setOnClickListener {
            Toast.makeText(context, "ALREADY HERE", Toast.LENGTH_SHORT).show()
            //findNavController().navigate(R.id.action_InsideHouseFragment_to_ShopFragment)
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
            }
            else { Toast.makeText(context, "Not enough gold!", Toast.LENGTH_SHORT).show() }
        }
        binding.purchasePesticide.setOnClickListener {

            if (player.gold >= 30)
            {
                shop.buyPesticide(player, 30)
                binding.coinBalance.text = player.gold.toString()
            }
            else { Toast.makeText(context, "Not enough gold!", Toast.LENGTH_SHORT).show() }
        }
        binding.purchaseRose.setOnClickListener {

            if (player.gold >= FlowerType.ROSE.seedCost)
            {
                shop.buySeed(player, FlowerType.ROSE)
                binding.coinBalance.text = player.gold.toString()
            }
            else { Toast.makeText(context, "Not enough gold!", Toast.LENGTH_SHORT).show() }
        }
        binding.purchaseTulip.setOnClickListener {
            if (player.gold >= FlowerType.TULIP.seedCost)
            {
                shop.buySeed(player, FlowerType.TULIP)
                binding.coinBalance.text = player.gold.toString()
            }
            else { Toast.makeText(context, "Not enough gold!", Toast.LENGTH_SHORT).show() }
        }
        binding.purchaseLily.setOnClickListener {
            if (player.gold >= FlowerType.LILY.seedCost)
            {
                shop.buySeed(player, FlowerType.LILY)
                binding.coinBalance.text = player.gold.toString()
            }
            else { Toast.makeText(context, "Not enough gold!", Toast.LENGTH_SHORT).show() }
        }
        binding.purchaseDahlia.setOnClickListener {
            if (player.gold >= FlowerType.DAHLIA.seedCost)
            {
                shop.buySeed(player, FlowerType.DAHLIA)
                binding.coinBalance.text = player.gold.toString()
            }
            else { Toast.makeText(context, "Not enough gold!", Toast.LENGTH_SHORT).show() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shopFragmentBinding = null
    }
}