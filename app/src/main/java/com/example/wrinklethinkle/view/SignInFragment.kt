package com.example.wrinklethinkle.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.viewmodel.SignInScreenViewModel
import com.example.wrinklethinkle.databinding.FragmentSignInScreenBinding

class SignInFragment : Fragment() {
    private var signInFragment: FragmentSignInScreenBinding? = null
    private val binding get() = signInFragment!!

    private val viewModel: SignInScreenViewModel by viewModels()
    var emailText = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        // Inflate the layout for this fragment
        signInFragment = FragmentSignInScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).build()
        binding.signInScreenSignupButton.setOnClickListener {
            Toast.makeText(context, "DO STUFF", Toast.LENGTH_SHORT).show()
            navController.navigate(R.id.action_signInFragment_to_signUpFragment, null, navOptions)
        }

        /*binding.signInScreen.setOnClickListener {
            emailText = binding.signInScreenEmail.text.toString()
            Toast.makeText(context, emailText, Toast.LENGTH_SHORT).show()
            Log.d("Test", emailText)
        }*/

    }


}