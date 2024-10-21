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
import android.widget.ImageView
import androidx.core.content.ContextCompat

class GrowFragment : Fragment() {

    private var growFragmentBinding: GrowFragmentBinding? = null
    private lateinit var selectedFlowerType: FlowerType
    private lateinit var mediaPlayer: MediaPlayer
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private val growBackgroundViewModel: GrowBackgroundViewModel by activityViewModels() // ViewModel for background and available flowers
    private var clickCount: Double = 0.0
    private var growthStage = 0
    private var bugCount = 0
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

        // Set default GrowScreen background and flowers if not set
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

        binding.pesticideButton.setOnClickListener {
            applyPesticide()
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
                    val actualClickPower = (1 * player.clickPower) / (1 + (bugCount * 0.5))

                    clickCount += actualClickPower

                    if (clickCount >= 50) {
                        clickCount = 0.0
                        growFlower()
                    }

                    val chance = (1..100).random()
                    if (chance <= 5){
                        spawnBug()
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

    private fun spawnBug(){
        // Increase bug count
        bugCount++

        // Display bug on the flower
        displayBugOnFlower()

        Toast.makeText(context, "A bug has been spotted!", Toast.LENGTH_SHORT).show()
    }

    private fun displayBugOnFlower() {
        // Define the bug image size
        val bugSize = 200

        // Create a new ImageView for the bug
        val bugImage = ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(bugSize, bugSize)
            setImageResource(R.drawable.bug_image)
            tag = "bug"
        }

        // Add the bug image to the flower layout
        binding.imageGroup.addView(bugImage)

        // Calculate random positions, ensuring the bug stays within the bounds
        val maxX = binding.flowerImage.width - bugSize
        val maxY = binding.flowerImage.height - bugSize

        // Generate random x and y within the available bounds
        val randomX = (0..maxX).random()
        val randomY = (0..maxY).random()

        // Position the bug image randomly within the bounds of the flower image
        bugImage.x = binding.flowerImage.x + randomX
        bugImage.y = binding.flowerImage.y + randomY
    }

    private fun clearBugImages() {
        val bugViews = mutableListOf<View>()
        // Collect all bug views to remove them
        for (i in 0 until binding.imageGroup.childCount) {
            val view = binding.imageGroup.getChildAt(i)
            if (view is ImageView && view.tag == "bug") {
                bugViews.add(view)  // Add to list for removal
            }
        }
        // Remove all collected bug views
        for (bugView in bugViews) {
            binding.imageGroup.removeView(bugView)
        }
    }

    private fun applyPesticide() {
        playerViewModel.playerData.value?.let { player ->
            if (player.pesticide > 0) {
                if (bugCount > 0) {
                    bugCount = 0
                    clearBugImages()
                    player.pesticide-- // Decrease the pesticide count
                    playerViewModel.setPlayerData(player) // Update the player data in ViewModel or Firebase
                    Toast.makeText(context, "Bugs cleared! Remaining pesticide: ${player.pesticide}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No bugs to clear!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "No pesticide available!", Toast.LENGTH_SHORT).show()
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
                    clickCount = 0.0
                    player.removeSeed(selectedFlowerType.name, 1)
                }
                .create()

            dialog.show()
        }
    }

    private fun resetGrowScreen() {
        binding.flowerImage.visibility = View.GONE
        growthStage = 0
        bugCount = 0
        clearBugImages()
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
