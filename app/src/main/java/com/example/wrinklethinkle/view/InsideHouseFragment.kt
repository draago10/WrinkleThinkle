package com.example.wrinklethinkle.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.databinding.FragmentInsideHouseBinding

class InsideHouseFragment : Fragment() {
    private var insideHouseFragmentBinding: FragmentInsideHouseBinding? = null
    private val binding get() = insideHouseFragmentBinding!!

    // Selected image resource ID, default to some image
    private var selectedImageResId: Int = R.drawable.black_dahlia_flower_sprout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        insideHouseFragmentBinding = FragmentInsideHouseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up existing navigation buttons
        binding.HouseGrowButton.setOnClickListener {
            findNavController().navigate(R.id.action_InsideHouseFragment_to_GrowFragment)
        }
        binding.HouseInventoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_InsideHouseFragment_to_InventoryFragment)
        }
        binding.HouseShopButton.setOnClickListener {
            findNavController().navigate(R.id.action_InsideHouseFragment_to_ShopFragment)
        }
        binding.HouseMapButton.setOnClickListener {
            //findNavController().navigate(R.id.action_InsideHouseFragment_to_MapFragment)
        }

        // Add touch listener to the root layout to detect clicks
        binding.InsideHouseFragment.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Show the dialog to select an image at the click location
                showImageSelectionDialog(event.x, event.y)
            }
            true
        }
    }

    private fun showImageSelectionDialog(x: Float, y: Float) {
        // Array of image resources to choose from
        val images = arrayOf(R.drawable.black_dahlia_flower_complete, R.drawable.icon_grow, R.drawable.error_screen_cat)

        // Show a dialog for selecting the image
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select an image")
        builder.setItems(arrayOf("Dahlia", "Leaves", "POOPCAT")) { dialog, which ->
            // Set selected image resource
            selectedImageResId = images[which]
            // Place the image at the selected location
            placeImageAtLocation(x, y)
        }
        builder.show()
    }

    private fun placeImageAtLocation(x: Float, y: Float) {
        // Create a new ImageView and set its image resource
        val imageView = ImageView(requireContext()).apply {
            setImageResource(selectedImageResId)
            layoutParams = ViewGroup.LayoutParams(200, 200) // Adjust size as needed
            this.x = x - 100 // Adjust to center image on the click point
            this.y = y - 200
        }

        // Add the new ImageView to the root layout
        binding.InsideHouseFragment.addView(imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        insideHouseFragmentBinding = null
    }
}