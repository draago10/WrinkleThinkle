package com.example.wrinklethinkle.view
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.wrinklethinkle.R
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.viewmodel.SignInScreenViewModel
import com.example.wrinklethinkle.databinding.FragmentSignInScreenBinding
import com.example.wrinklethinkle.viewmodel.PlayerViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    // Nullable binding object for the fragment's view binding
    private var signInFragment: FragmentSignInScreenBinding? = null
    // Getter to safely access the binding once it's initialized
    private val binding get() = signInFragment!!

    // ViewModel for the sign-in screen to handle authentication and user data
    private val viewModel: SignInScreenViewModel by viewModels()
    // Shared ViewModel for player data across different fragments
    private val playerViewModel: PlayerViewModel by activityViewModels()
    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    // Variables to hold user input for email and password
    private var emailText = " "
    private var passwordText = ""
    // To store error messages that will be displayed in case of failed sign-in attempts
    private var errorMessage = ""

    // Called when the fragment is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase for authentication
        FirebaseApp.initializeApp(requireContext())
        // Get the FirebaseAuth instance
        auth = FirebaseAuth.getInstance()
    }

    // Inflate the fragment's layout using view binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for the SignInFragment using view binding
        signInFragment = FragmentSignInScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Called once the view is created to set up UI logic and interaction
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the navigation controller to handle navigation between fragments
        val navController = findNavController()
        // Define navigation options with custom animations for fragment transitions
        val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).build()

        // Auto-login: Check if the user is already signed in
        if (auth.currentUser?.uid != null) {
            // If a user is logged in, fetch user data
            auth.currentUser?.uid?.let { userId ->
                viewModel.fetchUserData(userId)
            }
        }

        // Observe changes in player data from the ViewModel
        viewModel.playerData.observe(viewLifecycleOwner) { player ->
            player?.let {
                // Set player data in the shared PlayerViewModel
                playerViewModel.setPlayerData(it)
                // Navigate to the inside house fragment upon successful login
                val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_up).build()
                navController.navigate(R.id.action_signInFragment_to_insideHouseFragment, null, navOptions)
            }
        }

        // Observe sign-in result (whether sign-in was successful or not)
        viewModel.signInResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                // On successful sign-in, fetch user data using the current user's UID
                auth.currentUser?.uid?.let { userId ->
                    viewModel.fetchUserData(userId)
                }
            } else {
                // Show error popup if sign-in fails
                Utility().showErrorPopup(
                    childFragmentManager,
                    requireContext(),
                    R.drawable.error_screen_cat,
                    "Oops, something went wrong...",
                    errorMessage,
                    { poop() } // Placeholder function to handle error popup action
                )
            }
        }

        // Observe any error message updates from the ViewModel
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            // Update the error message variable with the latest error
            errorMessage = error
        }

        // Set click listener for the "Sign Up" button to navigate to the SignUpFragment
        binding.signInScreenSignupButton.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment, null, navOptions)
        }

        // Set click listener for the "Sign In" button to handle the sign-in process
        binding.signInScreenSigninButton.setOnClickListener {
            // Check if email and password fields are not empty before signing in
            if (binding.signInScreenEmail.text.toString().isEmpty() || binding.signInScreenPassword.text.toString().isEmpty()) {
                // Show an error popup if fields are empty
                Utility().showErrorPopup(
                    childFragmentManager,
                    requireContext(),
                    R.drawable.error_screen_cat,
                    "Oops, empty fields detected!",
                    "Please do not leave the fields empty.",
                    { poop() } // Placeholder function for error popup action
                )
            } else {
                // Get the trimmed email and password from input fields
                emailText = binding.signInScreenEmail.text.toString().trim()
                passwordText = binding.signInScreenPassword.text.toString().trim()
                // Call ViewModel method to sign in the user with the provided email and password
                viewModel.signInUser(emailText, passwordText)
            }
        }
    }

    // Placeholder function for handling popup action
    fun poop() {
        // Can be replaced with actual functionality if needed
    }

    // Cleanup to prevent memory leaks by setting the binding object to null
    override fun onDestroyView() {
        super.onDestroyView()
        signInFragment = null
    }
}
