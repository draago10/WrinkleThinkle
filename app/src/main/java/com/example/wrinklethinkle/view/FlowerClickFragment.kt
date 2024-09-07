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
import com.example.wrinklethinkle.databinding.FlowerClickFragmentBinding
import com.example.wrinklethinkle.viewmodel.FlowerClickViewModel

class FlowerClickFragment : Fragment() {

    private var flowerFragmentBinding: FlowerClickFragmentBinding? = null
    private var plantTest = Plant()
    private val maxAlpha = 255
    private var currentAlpha = 10
    private val viewModel: FlowerClickViewModel by viewModels()

    private val binding get() = flowerFragmentBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        flowerFragmentBinding = FlowerClickFragmentBinding.inflate(inflater, container, false)
        plantTest.imageResource = R.drawable.flower_click_fragment_plant_pot
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
        flowerFragmentBinding = null
    }

}