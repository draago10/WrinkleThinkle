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
import com.example.wrinklethinkle.model.BlackDahlia
import com.example.wrinklethinkle.viewmodel.GrowViewModel
import android.media.MediaPlayer

class GrowFragment : Fragment() {

    private var growFragmentBinding: GrowFragmentBinding? = null
    private var test = BlackDahlia
    private val viewModel: GrowViewModel by viewModels()
    private var currentImageIndex = 0
    private lateinit var mediaPlayer: MediaPlayer

    private val binding get() = growFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        growFragmentBinding = GrowFragmentBinding.inflate(inflater, container, false)
        mediaPlayer = MediaPlayer.create(context, R.raw.splash_sound)


        //binding.flowerImage.imageAlpha = currentAlpha
       //binding.rainButtonProgressbar.max = maxAlpha
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.selectedFlower(test)

        // Set the first image initially
        test.pieces?.let {
            if (it.isNotEmpty()) {
                binding.flowerImage.setImageResource(it[currentImageIndex])
                binding.flowerPotImage.setImageResource(R.drawable.icon_pot_blue)
            }
        }


        viewModel.clickCount.observe(viewLifecycleOwner, Observer { count ->
            binding.textCount.text = "Count: $count"
        })

//        viewModel.currentAlpha.observe(viewLifecycleOwner, Observer { alpha ->
//            binding.flowerImage.imageAlpha = alpha
//        })

        viewModel.isComplete.observe(viewLifecycleOwner, Observer { isComplete ->
            if (isComplete) {
                Toast.makeText(context, "Complete!", Toast.LENGTH_SHORT).show()

                test.pieces?.let { pieces ->
                    if (currentImageIndex < pieces.size - 1) {
                        // Move to the next image
                        currentImageIndex++
                        binding.flowerImage.setImageResource(pieces[currentImageIndex])

                        // Reset state in ViewModel for the new image
                        viewModel.reset()

                    } else {
                        Toast.makeText(context, "No more images to display", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        val shrinkGrowAnimation = AnimationUtils.loadAnimation(context, R.anim.shrink_and_grow)
        // Increment count when button is clicked
        binding.flowerImage.setOnClickListener {
            viewModel.incrementCount()
            binding.imageGroup.startAnimation(shrinkGrowAnimation)
            playSound()
        }

        binding.goHomeButton.setOnClickListener {
            
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


    override fun onDestroyView() {
        super.onDestroyView()
        growFragmentBinding = null
        if (this::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }

}