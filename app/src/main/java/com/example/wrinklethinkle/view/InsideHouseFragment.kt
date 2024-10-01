package com.example.wrinklethinkle.view

import android.annotation.SuppressLint
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
import com.example.wrinklethinkle.model.Player
import com.example.wrinklethinkle.model.Inventory
import com.example.wrinklethinkle.model.Flower

class InsideHouseFragment : Fragment() {
    private var insideHouseFragmentBinding: FragmentInsideHouseBinding? = null
    private val binding get() = insideHouseFragmentBinding!!

    // Selected image resource ID, default to some image
    private var selectedImageResId: Int = R.drawable.black_dahlia_flower_sprout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        insideHouseFragmentBinding = FragmentInsideHouseBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
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
                // Get the height of the navbar (the restricted area)
                val navbarHeight = binding.NavbarHouse.height
                val screenHeight = resources.displayMetrics.heightPixels

                // Check if the click is above the navbar area
                if (event.y < (screenHeight - navbarHeight)) {
                    // Show the dialog to select an image at the click location if outside navbar
                    showImageSelectionDialog(event.x, event.y)
                }
            }
            true
        }
    }

    private fun showImageSelectionDialog(x: Float, y: Float) {
        // Get the list of completed flowers from the inventory
        val completedFlowers = Player.inventory.completedFlowers

        // Check to make sure there are completed flowers in inventory
        if (completedFlowers.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("No Completed Flowers")
                .setMessage("There are no completed flowers in your inventory")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .show()
            return
        }

        // Extract the names of the completed flowers
        val flowerNames = completedFlowers.map { it.type.name }.toTypedArray()

        // Extract the image resource ID of the completed flowers
        val images = completedFlowers.map { it.type.image }.toTypedArray()

        // Show a dialog for selecting the image
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select an image")

        // Use flowerNames in the dialog
        builder.setItems(flowerNames) { dialog, which ->
            // Set selected image resource by indexing into the images list
            selectedImageResId = images[which]
            // Place the image at the selected location
            placeImageAtLocation(x, y)
        }

        // Show the dialog
        builder.show()
    }

    private fun placeImageAtLocation(x: Float, y: Float) {
        // Create a new ImageView and set its image resource
        val imageView = ImageView(requireContext()).apply {
            setImageResource(selectedImageResId)
            layoutParams = ViewGroup.LayoutParams(200, 200)
            this.x = x - 100
            this.y = y - 200
        }

        // Add the new ImageView to the root layout
        binding.InsideHouseFragment.addView(imageView)

        imageView.setOnLongClickListener { v ->
            AlertDialog.Builder(requireContext())
                .setTitle("Remove Image")
                .setMessage("Are you sure you want to remove this image?")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    (v?.parent as? ViewGroup)?.removeView(v)
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        insideHouseFragmentBinding = null
    }
}