package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
//import androidx.fragment.app.viewModels
import com.example.wrinklethinkle.R
import android.view.animation.AnimationUtils
import com.example.wrinklethinkle.databinding.GrowFragmentBinding
import android.media.MediaPlayer
import android.app.AlertDialog
import com.example.wrinklethinkle.model.*

class GrowFragment : Fragment() {

    private var growFragmentBinding: GrowFragmentBinding? = null
    private lateinit var selectedFlowerType: FlowerType
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var player: PlayerCharacter
    private var clickCount = 0
    private var growthStage = 0  // New property to track growth stage

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
        showSeedSelectionDialog(player)

        val shrinkGrowAnimation = AnimationUtils.loadAnimation(context, R.anim.shrink_and_grow)

        // Handle click events on the flower image to grow the flower
        binding.flowerImage.setOnClickListener {
            if (growthStage < 4) {  // Limit growth stages to 4
                // Increase the click count based on player clickPower
                clickCount += (1 * player.clickPower).toInt()

                // If clickCount reaches 50, grow the flower and reset clickCount
                if (clickCount >= 50) {
                    clickCount = 0  // Reset click count after reaching 50
                    growFlower()
                    binding.imageGroup.startAnimation(shrinkGrowAnimation)
                    playSound()
                }
            } else {
                // Handle fully grown flower
                Toast.makeText(context, "Flower fully grown!", Toast.LENGTH_SHORT).show()
                player.addFlower(selectedFlowerType.name, 1) // Add flower to player's inventory
                resetGrowScreen()
            }
        }
    }

    private fun growFlower() {
        // Update the growth stage
        growthStage++

        // Update the displayed image based on the current growth stage
        when (growthStage) {
            1 -> binding.flowerImage.setImageResource(selectedFlowerType.seedImage)
            2 -> binding.flowerImage.setImageResource(selectedFlowerType.sproutImage)
            3 -> binding.flowerImage.setImageResource(selectedFlowerType.buddingImage)
            4 -> binding.flowerImage.setImageResource(selectedFlowerType.flowerImage)
        }
    }

    private fun playSound() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepare()
        }
        mediaPlayer.start()
    }

    private fun showSeedSelectionDialog(player: PlayerCharacter) {
        val seedMenuItems = mutableListOf<String>()

        for ((flowerName, amount) in player.seeds) {
            if (amount > 0) {
                seedMenuItems.add(flowerName)
            }
        }

        val dialog = AlertDialog.Builder(context)
            .setTitle("Select a Seed to Grow")
            .setItems(seedMenuItems.toTypedArray()) { _, which ->
                val selectedSeed = seedMenuItems[which]
                selectedFlowerType = FlowerType.valueOf(selectedSeed.uppercase()) // Update selectedFlowerType
                binding.flowerImage.setImageResource(selectedFlowerType.seedImage)
                growthStage = 1 // Reset growth stage when new seed is selected
                clickCount = 0  // Reset click count when a new flower is selected
            }
            .create()

        dialog.show()
    }

    private fun resetGrowScreen() {
        binding.flowerImage.setImageResource(R.drawable.icon_pot_blue) // Reset to placeholder image
        growthStage = 0 // Reset growth stage
        clickCount = 0 // Reset click count
    }

    override fun onDestroyView() {
        super.onDestroyView()
        growFragmentBinding = null
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}