package com.example.wrinklethinkle.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.databinding.FragmentSignUpBinding
import com.example.wrinklethinkle.viewmodel.SignUpScreenViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private var signUpFragment: FragmentSignUpBinding? = null
    private val binding get() = signUpFragment!!

    private val viewModel: SignUpScreenViewModel by viewModels()
    private  lateinit var auth: FirebaseAuth
    var email = ""
    var password = ""
    var errorMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        signUpFragment = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // auth = FirebaseAuth.getInstance()

        viewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                val navController = findNavController()
                val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_up).build()
                navController.navigate(R.id.action_signUpFragment_to_appStartFragment, null, navOptions)
            } else {
                Utility().showErrorPopup(childFragmentManager, requireContext(), "Oops, something went wrong...", errorMessage, { poop() })
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                error.let {
                    errorMessage = it
                }
        }

        binding.signUpScreenSignupButton.setOnClickListener {
            if (binding.signUpScreenEmail.text.toString().isEmpty() || binding.signUpScreenPassword.text.toString().isEmpty()) {
                Utility().showErrorPopup(childFragmentManager, requireContext(), "Oops, empty fields detected!", "Please do not leave the fields empty.", {poop()})
            } else {
                email = binding.signUpScreenEmail.text.toString().trim()
                password = binding.signUpScreenPassword.text.toString().trim()
                viewModel.createUser(email, password)
            }
        }
    }

    fun poop() {
    }


    override fun onDestroyView() {
        super.onDestroyView()
        signUpFragment = null
    }
}