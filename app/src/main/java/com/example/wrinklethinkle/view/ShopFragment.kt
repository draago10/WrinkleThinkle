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
    }

    override fun onDestroy() {
        super.onDestroy()
        shopFragmentBinding = null
    }
}