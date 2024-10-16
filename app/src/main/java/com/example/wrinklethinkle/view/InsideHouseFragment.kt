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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.databinding.FragmentInsideHouseBinding
import com.example.wrinklethinkle.model.PlayerCharacter
import com.example.wrinklethinkle.model.FlowerType
import com.example.wrinklethinkle.viewmodel.PlayerViewModel
import com.google.firebase.auth.FirebaseAuth

class InsideHouseFragment : Fragment() {
    private var insideHouseFragmentBinding: FragmentInsideHouseBinding? = null
    private val binding get() = insideHouseFragmentBinding!!
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val playerViewModel: PlayerViewModel by activityViewModels()

    // Selected image resource ID, default to some image
    private var selectedImageResId: Int = R.drawable.black_dahlia_flower_sprout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
            // Navigation to MapFragment (if implemented)
        }

        // Logout function
        binding.logoutIcon.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.action_logout_user)
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
                    playerViewModel.playerData.observe(viewLifecycleOwner) { player ->
                        showImageSelectionDialog(player,event.x, event.y)
                    }
                }
            }
            true
        }
    }

    private fun showImageSelectionDialog(player: PlayerCharacter,x: Float, y: Float) {
        // Get the list of flowers from the PlayerCharacter's flowers map
        val availableFlowers = player.flowers.filter { it.value > 0 }

        // Check to make sure there are available flowers in inventory
        if (availableFlowers.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("No Completed Flowers")
                .setMessage("There are no completed flowers in your inventory")
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .show()
            return
        }

        // Extract the flower names from the available flowers map
        val flowerNames = availableFlowers.keys.toTypedArray()

        // Extract the flower images from the FlowerType enum based on the flower name
        val images = flowerNames.map { flowerName ->
            FlowerType.valueOf(flowerName.uppercase()).flowerImage // Ensure the flower name matches enum case
        }.toTypedArray()

        // Show a dialog for selecting the image
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select an image")

        // Use flowerNames in the dialog
        builder.setItems(flowerNames) { _, which ->
            // Set selected image resource by indexing into the images list
            selectedImageResId = images[which]

            // Place the image at the selected location
            placeImageAtLocation(player, x, y, flowerNames[which])
            player.removeFlower(flowerNames[which], 1)
        }

        // Show the dialog
        builder.show()
    }

    private fun placeImageAtLocation(player: PlayerCharacter,x: Float, y: Float, flowerName: String) {
        // Create a new ImageView and set its image resource
        val imageView = ImageView(requireContext()).apply {
            setImageResource(selectedImageResId)
            layoutParams = ViewGroup.LayoutParams(200, 200)
            this.x = x - 100 // Adjust placement as needed
            this.y = y - 200

            tag = flowerName
        }

        // Add the new ImageView to the root layout
        binding.InsideHouseFragment.addView(imageView)

        // Set up long click listener to allow removal of the placed image
        imageView.setOnLongClickListener { v ->
            AlertDialog.Builder(requireContext())
                .setTitle("Remove Image")
                .setMessage("Are you sure you want to remove this image?")
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    // Remove the image from the layout
                    (v?.parent as? ViewGroup)?.removeView(v)

                    // Retrieve the flower name from the tag and add it back to the player's flowers map
                    val flowerNameToAdd = v?.tag as? String
                    if (flowerNameToAdd != null) {
                        player.addFlower(flowerNameToAdd, 1)
                    }
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