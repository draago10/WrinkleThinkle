package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.wrinklethinkle.model.Plant
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.databinding.FragmentFirstBinding
import com.example.wrinklethinkle.viewmodel.FirstFragmentViewModel
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
    private val viewModel: FirstFragmentViewModel by viewModels()
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
        binding.rainButtonProgressbar.max = maxAlpha
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.clickCount.observe(viewLifecycleOwner, Observer { count ->
            binding.textCount.text = "Count: $count"
        })

        viewModel.currentAlpha.observe(viewLifecycleOwner, Observer { alpha ->
            binding.flowerImage.imageAlpha = alpha
            binding.rainButtonProgressbar.progress = alpha
        })

        // Observe completion status
        viewModel.isComplete.observe(viewLifecycleOwner, Observer { isComplete ->
            if (isComplete) {
                Toast.makeText(context, "Complete!", Toast.LENGTH_SHORT).show()
                binding.rainButton.isEnabled = false
                binding.rainButton.isActivated = false
            }
        })
        //binding.backgroundImage.setImageResource(R.drawable.background)
        binding.rainButton.setOnClickListener {
            viewModel.incrementCount()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}