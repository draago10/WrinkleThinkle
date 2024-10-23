package com.example.wrinklethinkle.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.databinding.FragmentSignUpBinding
import com.example.wrinklethinkle.model.PlayerCharacter
import com.example.wrinklethinkle.viewmodel.PlayerViewModel
import com.example.wrinklethinkle.viewmodel.SignUpScreenViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    // Binding for the fragment's layout, initialized as null
    private var signUpFragment: FragmentSignUpBinding? = null

    // Property to get the binding safely (using !! to assert it's non-null)
    private val binding get() = signUpFragment!!

    // Firebase authentication instance, initialized lazily
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // ViewModel for managing sign-up screen state
    private val viewModel: SignUpScreenViewModel by viewModels()

    // ViewModel to share player data across fragments
    private val playerViewModel: PlayerViewModel by activityViewModels()

    // Variables to hold user input and error messages
    private var email = ""
    private var password = ""
    private var playerName = ""
    private var errorMessage = ""

    // Lifecycle method triggered when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase app
        FirebaseApp.initializeApp(requireContext())
    }

    // Lifecycle method triggered to create the view for the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using View Binding
        signUpFragment = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Lifecycle method triggered after the view is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the navigation controller
        val navController = findNavController()
        // Set up navigation options with an enter animation
        val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_up).build()

        // Observing sign-up result from the ViewModel
        viewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                // If sign-up was successful, fetch player data using the user ID
                auth.currentUser?.uid?.let { userId ->
                    viewModel.fetchPlayerData(userId)
                }
            } else {
                // Show error popup in case of failure
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.error_screen_cat,"Oops, something went wrong...", errorMessage, { poop() })
            }
        }

        // Observing error messages from the ViewModel
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            errorMessage = error // Update the error message
        }

        // Observing player data and navigating if it is set
        viewModel.playerData.observe(viewLifecycleOwner) { player ->
            player?.let {
                // Set player data in the shared PlayerViewModel
                playerViewModel.setPlayerData(it)
                // Navigate to the next fragment
                navController.navigate(R.id.action_signUpFragment_to_insideHouseFragment, null, navOptions)
            }
        }

        // Set click listener on the sign-up button
        binding.signUpScreenSignupButton.setOnClickListener {
            // Check for empty fields before attempting to sign up
            if (binding.signUpScreenEmail.text.toString().isEmpty() || binding.signUpScreenPassword.text.toString().isEmpty()) {
                // Show error popup for empty fields
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.error_screen_cat, "Oops, empty fields detected!", "Please do not leave the fields empty.", { poop() })
            } else {
                // Trim and store input values
                email = binding.signUpScreenEmail.text.toString().trim()
                password = binding.signUpScreenPassword.text.toString().trim()
                playerName = binding.signUpScreenPlayerName.text.toString().trim()
                // Call the ViewModel to create a new user
                viewModel.createUser(email, password, playerName)
            }
        }
    }

    // Placeholder function for handling actions related to error popups
    fun poop() {
        // Placeholder function for error popup action
    }

    // Lifecycle method triggered when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding reference to avoid memory leaks
        signUpFragment = null
    }
}