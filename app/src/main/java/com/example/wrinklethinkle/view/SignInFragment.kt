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
    private var signInFragment: FragmentSignInScreenBinding? = null
    private val binding get() = signInFragment!!

    private val viewModel: SignInScreenViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    private var emailText = " "
    private var passwordText = ""
    private var errorMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        signInFragment = FragmentSignInScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).build()

        // Check if user is already signed in (Auto-login)
        if (auth.currentUser?.uid != null) {
            auth.currentUser?.uid?.let { userId ->
                viewModel.fetchUserData(userId)
            }
        }

        // Observe player data for auto-login
        viewModel.playerData.observe(viewLifecycleOwner) { player ->
            player?.let {
                playerViewModel.setPlayerData(it)
                val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_up).build()
                navController.navigate(R.id.action_signInFragment_to_insideHouseFragment, null, navOptions)
            }
        }

        // Manual sign-in flow
        viewModel.signInResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                auth.currentUser?.uid?.let { userId ->
                    viewModel.fetchUserData(userId)
                }
            } else {
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.error_screen_cat, "Oops, something went wrong...", errorMessage, { poop() })
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            errorMessage = error
        }

        binding.signInScreenSignupButton.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment, null, navOptions)
        }

        binding.signInScreenSigninButton.setOnClickListener {
            if (binding.signInScreenEmail.text.toString().isEmpty() || binding.signInScreenPassword.text.toString().isEmpty()) {
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.error_screen_cat, "Oops, empty fields detected!", "Please do not leave the fields empty.", { poop() })
            } else {
                emailText = binding.signInScreenEmail.text.toString().trim()
                passwordText = binding.signInScreenPassword.text.toString().trim()
                viewModel.signInUser(emailText, passwordText)
            }
        }
    }

    fun poop() {
        // Placeholder function for error popup action
    }

    override fun onDestroyView() {
        super.onDestroyView()
        signInFragment = null
    }
}
