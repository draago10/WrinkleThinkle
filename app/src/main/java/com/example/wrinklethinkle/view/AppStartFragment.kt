package com.example.wrinklethinkle.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.databinding.FragmentAppStartBinding
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController


class AppStartFragment : Fragment() {
    private var appStartFragment: FragmentAppStartBinding? = null
    private val binding get() = appStartFragment!!


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        appStartFragment = FragmentAppStartBinding.inflate(inflater, container, false)
        val pulseAnimation = AnimationUtils.loadAnimation(context, R.anim.pulse)
        binding.goHomeButton.startAnimation(pulseAnimation)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goHomeButton.setOnClickListener {
            findNavController().navigate(R.id.action_appStartFragment_to_FlowerClickFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        appStartFragment = null
    }
}