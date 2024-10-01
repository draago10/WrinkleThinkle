package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.wrinklethinkle.R
import android.view.animation.AnimationUtils
import com.example.wrinklethinkle.databinding.GrowFragmentBinding
import com.example.wrinklethinkle.viewmodel.GrowViewModel
import android.media.MediaPlayer
import com.example.wrinklethinkle.model.Flower
import com.example.wrinklethinkle.model.FlowerType
import com.example.wrinklethinkle.model.Player
import com.example.wrinklethinkle.model.Inventory
import android.app.AlertDialog

class GrowFragment : Fragment() {

    private var growFragmentBinding: GrowFragmentBinding? = null
    private val viewModel: GrowViewModel by viewModels()
    private lateinit var selectedFlower: Flower
    private lateinit var mediaPlayer: MediaPlayer
    private val player = Player

    private val binding get() = growFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        growFragmentBinding = GrowFragmentBinding.inflate(inflater, container, false)
        mediaPlayer = MediaPlayer.create(context, R.raw.splash_sound)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Show seed selection dialog at the start
        showSeedSelectionDialog()

        val shrinkGrowAnimation = AnimationUtils.loadAnimation(context, R.anim.shrink_and_grow)

        // Handle click events on the flower image to grow the flower
        binding.flowerImage.setOnClickListener {
            // Check if selectedFlower is initialized
            if (viewModel.selectedFlower != null) {
                val clickCount = viewModel.clickCount.value ?: 0

                // Call grow() to update growth and apply clicks
                selectedFlower.grow(clickCount, player)

                // Reset the ViewModel's count after applying the clicks
                viewModel.reset()

                // Update the displayed image based on the flower's growth stage
                selectedFlower.type.pieces?.let { pieces ->
                    if (selectedFlower.growthStage - 1 < pieces.size) {
                        binding.flowerImage.setImageResource(pieces[selectedFlower.growthStage - 1])
                    }
                }

                // Start the animation
                binding.imageGroup.startAnimation(shrinkGrowAnimation)
                playSound()

                // Check if the flower is fully grown
                if (selectedFlower.growthStage == Flower.MAX_GROWTH_STAGE) {
                    Toast.makeText(context, "Flower fully grown!", Toast.LENGTH_SHORT).show()
                    player.inventory.addCompletedFlower(selectedFlower) // Add flower to player's inventory
                    resetGrowScreen()
                }
            } else {
                // Error handling if selectedFlower is null
                Toast.makeText(requireContext(), "Flower not selected", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun playSound() {
        if (mediaPlayer.isPlaying) {
            // Stop the current sound if already playing
            mediaPlayer.stop()
            mediaPlayer.prepare() // Prepare for the next play
        }
        mediaPlayer.start()
    }

    private fun showSeedSelectionDialog() {
        // Get available seeds from inventory
        val availableSeeds = player.inventory.getAvailableSeeds()
        if (availableSeeds.isEmpty()) {
            Toast.makeText(context, "No seeds available!", Toast.LENGTH_SHORT).show()
            return
        }

        val seedNames = availableSeeds.map { it.name }.toTypedArray()
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select a seed to plant")
        builder.setItems(seedNames) { _, which ->
            val selectedSeedType = availableSeeds[which]
            selectedFlower = Flower(selectedSeedType)

            // Set initial image
            selectedFlower.type.pieces?.let { pieces ->
                binding.flowerImage.setImageResource(pieces[0])
                binding.flowerPotImage.setImageResource(R.drawable.icon_pot_blue)
            }

            player.inventory.removeSeed(selectedSeedType)
        }
        builder.show()
    }

    private fun resetGrowScreen() {
        selectedFlower = Flower(FlowerType.ROSE) // Reset to default flower or any choice
        binding.flowerImage.setImageResource(R.drawable.icon_pot_blue) // Placeholder image
    }

    override fun onDestroyView() {
        super.onDestroyView()
        growFragmentBinding = null
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}