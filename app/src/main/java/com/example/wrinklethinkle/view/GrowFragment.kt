package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ProgressBar // Import ProgressBar
import com.example.wrinklethinkle.R
import android.view.animation.AnimationUtils
import com.example.wrinklethinkle.databinding.GrowFragmentBinding
import android.media.MediaPlayer
import android.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.model.*
import com.example.wrinklethinkle.viewmodel.PlayerViewModel

class GrowFragment : Fragment() {

    private var growFragmentBinding: GrowFragmentBinding? = null
    private lateinit var selectedFlowerType: FlowerType
    private lateinit var mediaPlayer: MediaPlayer
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private var clickCount = 0
    private var growthStage = 0  // New property to track growth stage
    private lateinit var experienceProgressBar: ProgressBar // Progress bar for XP

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

        // Initialize the experience progress bar
        experienceProgressBar = binding.experienceProgressBar

        binding.goHomeButton.setOnClickListener {
            findNavController().navigate(R.id.action_GrowFragment_to_InsideHouseFragment)
        }
        binding.growFragShopIcon.setOnClickListener {
            findNavController().navigate(R.id.action_GrowFragment_to_ShopFragment)
        }
        binding.growInventoryButton.setOnClickListener {
            findNavController().navigate(R.id.action_GrowFragment_to_InventoryFragment)
        }
        binding.growMapButton.setOnClickListener {
            findNavController().navigate(R.id.action_GrowFragment_to_MapFragment)
        }
        binding.growIcon.setOnClickListener {
            Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.project_gnome,"Oh my gnome!", "You're already here :)")
        }

        val shrinkGrowAnimation = AnimationUtils.loadAnimation(context, R.anim.shrink_and_grow)

        playerViewModel.playerData.observe(viewLifecycleOwner) { player ->

            updateExperienceProgressBar(player) // Update the XP bar initially
            updatePlayerLevelText(player) // Update player level initially

            // Set click listener for the SeedSelection button to show seed selection dialog
            binding.SeedSelection.setOnClickListener {
                showSeedSelectionDialog(player)  // Show seed selection dialog when button is clicked
            }

            // Handle click events on the flower image to grow the flower
            binding.flowerImage.setOnClickListener {
                binding.imageGroup.startAnimation(shrinkGrowAnimation)
                playSound()
                if (growthStage < 4) {  // Limit growth stages to 4
                    // Increase the click count based on player clickPower
                    clickCount += (1 * player.clickPower).toInt()

                    // If clickCount reaches 50, grow the flower and reset clickCount
                    //  SET TO >= 1 FOR TESTING -------------------------------------------------------------
                    if (clickCount >= 10) {
                        clickCount = 0  // Reset click count after reaching 50
                        growFlower()
                    }
                } else {
                    // Handle fully grown flower
                    Toast.makeText(context, "Flower fully grown!", Toast.LENGTH_SHORT).show()

                    // Gain experience based on the flower's rank
                    val experienceGained = 50 * selectedFlowerType.flowerRank
                    player.gainExperience(experienceGained)
                    updateExperienceProgressBar(player) // Update progress bar after gaining XP
                    updatePlayerLevelText(player) // Update player level if experience changes

                    player.addFlower(selectedFlowerType.name, 1) // Add flower to player's flowers map
                    resetGrowScreen()
                }
            }
        }
    }

    // Function to update the progress bar with player's current experience
    private fun updateExperienceProgressBar(player: PlayerCharacter) {
        val currentExperience = player.experience
        val experienceToNextLevel = 100 * player.level
        val progress = (currentExperience.toDouble() / experienceToNextLevel.toDouble()) * 100
        experienceProgressBar.progress = progress.toInt()
    }

    // Function to update the player level TextView
    private fun updatePlayerLevelText(player: PlayerCharacter) {
        binding.playerLevelText.text = "${player.level}"
    }

    private fun growFlower() {
        if (growthStage >= 5) {
            return
        }
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
        if(player.seeds.isEmpty()) {
            Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.error_screen_cat, "Oops, empty fields detected!", "Please do not leave the fields empty.", { })
        } else {
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
                    binding.flowerImage.setImageResource(selectedFlowerType.sproutImage)
                    binding.flowerPotImage.setImageResource(R.drawable.icon_pot_blue)
                    binding.flowerImage.visibility = View.VISIBLE
                    growthStage = 1 // Reset growth stage when new seed is selected
                    clickCount = 0  // Reset click count when a new flower is selected
                    player.removeSeed(selectedFlowerType.name, 1) // Remove seed from player's seeds map
                }
                .create()

            dialog.show()
        }
    }

    private fun resetGrowScreen() {
        // remove the flower image when resetting screen after a flower is fully grown
        binding.flowerImage.visibility = View.GONE
        // reset growth stage and click count
        growthStage = 0
        clickCount = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        growFragmentBinding = null
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
