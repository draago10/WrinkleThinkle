package com.example.wrinklethinkle.view

import android.annotation.SuppressLint
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
import android.widget.TextView
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
    private var fertilizerIsOn = false;
    private lateinit var experienceProgressBar: ProgressBar
    private lateinit var clickPowerText: TextView

    private val binding get() = growFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        growFragmentBinding = GrowFragmentBinding.inflate(inflater, container, false)
        // Initialize the MediaPlayer with a sound resource
        mediaPlayer = MediaPlayer.create(context, R.raw.splash_sound)
        // Return the root view of the binding
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set default GrowScreen background and flowers if not set
        if (growBackgroundViewModel.backgroundImage.value == null) {
            // Background image
            growBackgroundViewModel.setBackgroundImage(R.drawable.grow_bg_garden)
            // Available flowers
            growBackgroundViewModel.setAvailableFlowers(listOf(FlowerType.ROSE, FlowerType.TULIP))
            // Active button
            growBackgroundViewModel.setActiveButton(R.id.GardenGrow)
        }


        clickPowerText = binding.root.findViewById(R.id.clickPowerText)

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


        // Button click listeners
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
        binding.fertilizerButton.setOnClickListener {
            toggleFertilizer()
        }
        binding.pesticideButton.setOnClickListener {
            applyPesticide()
        }

        val shrinkGrowAnimation = AnimationUtils.loadAnimation(context, R.anim.shrink_and_grow)

        // PLAYER VIEW MODEL -------------------------------------------------------------------------------------------------------
        playerViewModel.playerData.observe(viewLifecycleOwner) { player ->

            // Updates
            updateExperienceProgressBar(player)
            updatePlayerLevelText(player)
            updateClickPowerText(player)


            binding.SeedSelection.setOnClickListener {
                showSeedSelectionDialog(player)
            }

            // Handle click events on the flower image to grow the flower
            binding.flowerImage.setOnClickListener {

                // Animation and sound
                binding.imageGroup.startAnimation(shrinkGrowAnimation)
                playSound()

                // Click power logic
                if (growthStage < 4) {

                    // Click power with bug influence logic
                    var actualClickPower = ((1 * player.clickPower) / (1 + (bugCount * 0.5)))

                    // Click power with fertilizer influence logic
                    if (fertilizerIsOn && player.fertilizer >= 0) {
                        // If fertilizer is finished
                        if (player.fertilizer == 0){
                            Toast.makeText(context, "You're all out of fertilizers!", Toast.LENGTH_SHORT).show()
                            fertilizerIsOn = false
                        }
                        // Otherwise, click power doubled
                        else {
                            actualClickPower *= 2
                            player.removeFertilizer(1)

                        }
                    }

                    // Add click power to the total clicks bucket
                    clickCount += actualClickPower
                    updateClickPowerText(player)

                    // If the bucket is full (50 total clicks)
                    if (clickCount >= 50) {
                        // Empty the bucket and grow the flower
                        clickCount = 0.0
                        growFlower()
                    }

                    // 5% chance of spawning a bug
                    val chance = (1..100).random()
                    if (chance <= 5){
                        spawnBug()
                    }

                // If the growth stage is not less than 4 then the flower is fully grown
                } else {
                    // User feedback
                    Toast.makeText(context, "Flower fully grown!", Toast.LENGTH_SHORT).show()

                    // Increase the player's xp
                    val experienceGained = 50 * selectedFlowerType.flowerRank
                    player.gainExperience(experienceGained)
                    updateExperienceProgressBar(player)

                    // Add complete flower to player inventory
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

        // User feeback
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

    private fun toggleFertilizer() {
        if (fertilizerIsOn) {
            // toggle fertilizer off if it was previously on
            fertilizerIsOn = false
            // User feedback with amount of fertlizer left
            playerViewModel.playerData.value?.let { player ->
                Toast.makeText(context, "Fertilizer is off (" + player.fertilizer + " left)!", Toast.LENGTH_SHORT).show()
            }
        }
        else {
            playerViewModel.playerData.value?.let { player ->
                // Case: if no fertilizer left...
                if (player.fertilizer <= 0) {
                    // Notify the user
                    Toast.makeText(context, "No fertilizer left!", Toast.LENGTH_SHORT).show()
                    // Keep fertilizer toggled off
                }
                // Otherwise
                else{
                    // toggle fertilizer on
                    fertilizerIsOn = true
                    // Notify user of amount left
                    Toast.makeText(context, "Fertilizer is on (" + player.fertilizer + " left)!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun applyPesticide() {
        // Get the current player's data
        playerViewModel.playerData.value?.let { player ->
            // Check if the player has any pesticide left
            if (player.pesticide > 0) {
                // Check if there are any bugs to clear
                if (bugCount > 0) {
                    // Clear the bugs and reset the bug count
                    bugCount = 0
                    clearBugImages() // Clear any bug images displayed
                    player.removePesticide(1) // Decrease pesticide count by 1
                    playerViewModel.setPlayerData(player) // Update the player data in ViewModel
                    // Show a toast message with remaining pesticide
                    Toast.makeText(context, "Bugs cleared! Remaining pesticide: ${player.pesticide}", Toast.LENGTH_SHORT).show()
                } else {
                    // Show a message if there are no bugs to clear
                    Toast.makeText(context, "No bugs to clear!", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show a message if the player has no pesticide available
                Toast.makeText(context, "No pesticide available!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateExperienceProgressBar(player: PlayerCharacter) {
        // Get the player's current experience and calculate progress to the next level
        val currentExperience = player.experience
        val experienceToNextLevel = 100 * player.level
        val progress = (currentExperience.toDouble() / experienceToNextLevel.toDouble()) * 100
        experienceProgressBar.progress = progress.toInt() // Update the progress bar
    }

    private fun updateClickPowerText(player: PlayerCharacter) {
        // Calculate actual click power based on player's click power and current bug count
        val actualClickPower = String.format("%.2f", ((1 * player.clickPower) / (1 + (bugCount * 0.5))))
        binding.clickPowerText.text = "Click Power: $actualClickPower" // Update the click power text
    }

    private fun updatePlayerLevelText(player: PlayerCharacter) {
        // Update the displayed player level
        binding.playerLevelText.text = "Level: ${player.level}"
    }

    private fun growFlower() {
        // Prevent growing the flower if it has reached max growth stage
        if (growthStage >= 5) {
            return
        }

        growthStage++ // Increment growth stage

        // Update the flower image based on the current growth stage
        when (growthStage) {
            1 -> binding.flowerImage.setImageResource(selectedFlowerType.seedImage)
            2 -> binding.flowerImage.setImageResource(selectedFlowerType.sproutImage)
            3 -> binding.flowerImage.setImageResource(selectedFlowerType.buddingImage)
            4 -> binding.flowerImage.setImageResource(selectedFlowerType.flowerImage)
        }
    }

    private fun playSound() {
        // Stop and prepare the media player if it is currently playing
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepare()
        }
        mediaPlayer.start() // Start playing sound
    }

    private fun showSeedSelectionDialog(player: PlayerCharacter) {
        val seedMenuItems = mutableListOf<String>() // List to hold available seeds

        // Only add seeds that are in the available flowers
        growBackgroundViewModel.availableFlowers.value?.let { availableFlowers ->
            for ((flowerName, amount) in player.seeds) {
                // Check if the player has seeds available and they are among the available flowers
                if (amount > 0 && availableFlowers.map { it.name }.contains(flowerName)) {
                    seedMenuItems.add(flowerName) // Add available seed to the menu
                }
            }
        }

        // If no available seeds, show an error popup
        if (seedMenuItems.isEmpty()) {
            Utility().showErrorPopup(
                childFragmentManager, requireContext(), R.drawable.error_screen_cat,
                "Oops, no available seeds!",
                "You do not have any seeds that can be planted here."
            )
        } else {
            // Create dialog for seed selection
            val dialog = AlertDialog.Builder(context)
                .setTitle("Select a Seed to Grow")
                .setItems(seedMenuItems.toTypedArray()) { _, which ->
                    // Handle the selected seed
                    val selectedSeed = seedMenuItems[which]
                    selectedFlowerType = FlowerType.valueOf(selectedSeed.uppercase()) // Set selected flower type
                    binding.flowerImage.setImageResource(selectedFlowerType.sproutImage) // Show sprout image
                    binding.flowerPotImage.setImageResource(R.drawable.icon_pot_blue) // Show flower pot image
                    binding.flowerImage.visibility = View.VISIBLE // Make flower image visible
                    growthStage = 1 // Reset growth stage
                    clickCount = 0.0 // Reset click count
                    player.removeSeed(selectedFlowerType.name, 1) // Remove one seed from the player
                }
                .create()

            dialog.show() // Show the seed selection dialog
        }
    }

    private fun resetGrowScreen() {
        // Reset the growth screen to initial state
        binding.flowerImage.visibility = View.GONE // Hide flower image
        growthStage = 0 // Reset growth stage
        bugCount = 0 // Reset bug count
        clearBugImages() // Clear any bug images displayed
    }

    override fun onDestroyView() {
        super.onDestroyView()
        growFragmentBinding = null // Clean up fragment binding
        // Release media player resources if it has been initialized
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }

    override fun onResume() {
        super.onResume()
        playerViewModel.fetchLatestPlayerData() // Fetch the latest player data when the fragment resumes
    }
}
