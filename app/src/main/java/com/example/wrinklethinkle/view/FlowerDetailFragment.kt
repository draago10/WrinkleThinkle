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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flower_detail, container, false)
    }
}