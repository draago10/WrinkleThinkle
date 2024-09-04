package com.example.wrinklethinkle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wrinklethinkle.databinding.FragmentFirstBinding
import kotlin.math.min
/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private var clickCount = 0
    private var plantTest = Plant()
    private val maxAlpha = 255            // Maximum alpha value
    private val alphaIncrement = 25       // Alpha increment per 25 clicks
    private val clicksPerIncrement = 25   // Number of clicks to increase alpha
    private var currentAlpha = 10

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        plantTest.imageResource = R.drawable.plant_pot
        binding.flowerImage.setImageResource(plantTest.imageResource)
        binding.flowerImage.imageAlpha = currentAlpha
        binding.progressBar.max = maxAlpha
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding.backgroundImage.setImageResource(R.drawable.background)
        binding.rainButton.setOnClickListener {
            incrementCount()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun incrementCount() {
        clickCount += 1
        binding.textCount.text = "Count: ${clickCount}"
        if (clickCount % clicksPerIncrement == 0) {
            // Increment the alpha by `alphaIncrement`
            currentAlpha += alphaIncrement
            // Ensure `currentAlpha` does not exceed `maxAlpha`
            currentAlpha = min(maxAlpha, currentAlpha)
        }


        // Update the ImageView's alpha
        binding.flowerImage.imageAlpha = currentAlpha
        binding.progressBar.progress = currentAlpha

    }

}