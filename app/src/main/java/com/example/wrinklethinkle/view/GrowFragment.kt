package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ProgressBar
import android.view.animation.AnimationUtils
import com.example.wrinklethinkle.R
import android.media.MediaPlayer
import android.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.databinding.GrowFragmentBinding
import com.example.wrinklethinkle.model.*
import com.example.wrinklethinkle.viewmodel.GrowBackgroundViewModel
import com.example.wrinklethinkle.viewmodel.PlayerViewModel

class GrowFragment : Fragment() {

    private var growFragmentBinding: GrowFragmentBinding? = null
    private lateinit var selectedFlowerType: FlowerType
    private lateinit var mediaPlayer: MediaPlayer
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private val growBackgroundViewModel: GrowBackgroundViewModel by activityViewModels() // ViewModel for background and available flowers
    private var clickCount = 0
    private var growthStage = 0
    private lateinit var experienceProgressBar: ProgressBar

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

        // Set default background and flowers if not set
        if (growBackgroundViewModel.backgroundImage.value == null) {
            growBackgroundViewModel.setBackgroundImage(R.drawable.grow_bg_garden)
            growBackgroundViewModel.setAvailableFlowers(listOf(FlowerType.ROSE, FlowerType.TULIP))
            growBackgroundViewModel.setActiveButton(R.id.GardenGrow)
        }

        // Observing background from GrowBackgroundViewModel
        growBackgroundViewModel.backgroundImage.observe(viewLifecycleOwner) { resourceId ->
            resourceId?.let {
                binding.root.setBackgroundResource(it)
            }
        }

        // Observing available flowers
        growBackgroundViewModel.availableFlowers.observe(viewLifecycleOwner) { flowers ->

        }

        // Initialize the experience progress bar
        experienceProgressBar = binding.experienceProgressBar

        // Observing background and available flowers from GrowBackgroundViewModel
        growBackgroundViewModel.backgroundImage.observe(viewLifecycleOwner) { resourceId ->
            resourceId?.let {
                binding.root.setBackgroundResource(it) // Set the background of the fragment
            }
        }

        growBackgroundViewModel.availableFlowers.observe(viewLifecycleOwner) { flowers ->

        }


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

            updateExperienceProgressBar(player)
            updatePlayerLevelText(player)

            binding.SeedSelection.setOnClickListener {
                showSeedSelectionDialog(player)
            }

            // Handle click events on the flower image to grow the flower
            binding.flowerImage.setOnClickListener {
                binding.imageGroup.startAnimation(shrinkGrowAnimation)
                playSound()
                if (growthStage < 4) {
                    clickCount += (1 * player.clickPower).toInt()

                    if (clickCount >= 10) { // Set to 10 for testing purposes
                        clickCount = 0
                        growFlower()
                    }
                } else {
                    Toast.makeText(context, "Flower fully grown!", Toast.LENGTH_SHORT).show()

                    val experienceGained = 50 * selectedFlowerType.flowerRank
                    player.gainExperience(experienceGained)
                    updateExperienceProgressBar(player)
                    updatePlayerLevelText(player)

                    player.addFlower(selectedFlowerType.name, 1) // Add flower to player's flowers map
                    resetGrowScreen()
                }
            }
        }
    }

    private fun updateExperienceProgressBar(player: PlayerCharacter) {
        val currentExperience = player.experience
        val experienceToNextLevel = 100 * player.level
        val progress = (currentExperience.toDouble() / experienceToNextLevel.toDouble()) * 100
        experienceProgressBar.progress = progress.toInt()
    }

    private fun updatePlayerLevelText(player: PlayerCharacter) {
        binding.playerLevelText.text = "${player.level}"
    }

    private fun growFlower() {
        if (growthStage >= 5) {
            return
        }

        growthStage++

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

        // Only add seeds that are in the available flowers
        growBackgroundViewModel.availableFlowers.value?.let { availableFlowers ->
            for ((flowerName, amount) in player.seeds) {
                if (amount > 0 && availableFlowers.map { it.name }.contains(flowerName)) {
                    seedMenuItems.add(flowerName)
                }
            }
        }

        if (seedMenuItems.isEmpty()) {
            // Show error if no available seeds
            Utility().showErrorPopup(
                childFragmentManager, requireContext(), R.drawable.error_screen_cat,
                "Oops, no available seeds!",
                "You do not have any seeds that can be planted here."
            )
        } else {
            // Create dialog with available seeds
            val dialog = AlertDialog.Builder(context)
                .setTitle("Select a Seed to Grow")
                .setItems(seedMenuItems.toTypedArray()) { _, which ->
                    val selectedSeed = seedMenuItems[which]
                    selectedFlowerType = FlowerType.valueOf(selectedSeed.uppercase())
                    binding.flowerImage.setImageResource(selectedFlowerType.sproutImage)
                    binding.flowerPotImage.setImageResource(R.drawable.icon_pot_blue)
                    binding.flowerImage.visibility = View.VISIBLE
                    growthStage = 1
                    clickCount = 0
                    player.removeSeed(selectedFlowerType.name, 1)
                }
                .create()

            dialog.show()
        }
    }

    private fun resetGrowScreen() {
        binding.flowerImage.visibility = View.GONE
        growthStage = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        growFragmentBinding = null
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.fetchLatestPlayerData()
    }
}
