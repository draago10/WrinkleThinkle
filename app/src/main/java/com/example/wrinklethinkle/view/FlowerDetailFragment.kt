package com.example.wrinklethinkle.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.databinding.GrowFragmentBinding
import com.example.wrinklethinkle.viewmodel.FlowerViewModel

class FlowerDetailFragment : Fragment() {
    private var flowerDetailFragmentBinding: FlowerDetailFragment? = null
    private val viewModel: FlowerViewModel by viewModels()
    private val binding get() = flowerDetailFragmentBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = "augA35caT0-s8xF3iwFPSZhf0TMIQteDZ_SoY57BSvg"
        var searchQuery = "coconut"

        viewModel.flowers.observe(this, Observer { flowers ->
            flowers.forEach { flower ->
                println("Plant Name: ${flower.bibliography}, Scientific Name: ${flower.scientific_name}")
            }
        })

        viewModel.error.observe(this, Observer { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        })

        viewModel.searchFlowers(token, searchQuery)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flower_detail, container, false)
    }
}