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

    private var signUpFragment: FragmentSignUpBinding? = null
    private val binding get() = signUpFragment!!
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val viewModel: SignUpScreenViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by activityViewModels()
    private var email = ""
    private var password = ""
    private var playerName = ""
    private var errorMessage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
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

        val navController = findNavController()
        val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_up).build()

        // Observe the sign-up result
        viewModel.signUpResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                auth.currentUser?.uid?.let { userId ->
                    viewModel.fetchPlayerData(userId)
                }
            } else {
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.error_screen_cat,"Oops, something went wrong...", errorMessage, { poop() })
            }
        }

        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            errorMessage = error
        }

        // Observe player data to ensure it is set before navigating
        viewModel.playerData.observe(viewLifecycleOwner) { player ->
            player?.let {
                playerViewModel.setPlayerData(it)
                navController.navigate(R.id.action_signUpFragment_to_insideHouseFragment, null, navOptions)
            }
        }

        binding.signUpScreenSignupButton.setOnClickListener {
            if (binding.signUpScreenEmail.text.toString().isEmpty() || binding.signUpScreenPassword.text.toString().isEmpty()) {
                Utility().showErrorPopup(childFragmentManager, requireContext(), R.drawable.error_screen_cat, "Oops, empty fields detected!", "Please do not leave the fields empty.", { poop() })
            } else {
                email = binding.signUpScreenEmail.text.toString().trim()
                password = binding.signUpScreenPassword.text.toString().trim()
                playerName = binding.signUpScreenPlayerName.text.toString().trim()
                viewModel.createUser(email, password, playerName)
            }
        }
    }

    fun poop() {
        // Placeholder function for error popup action
    }

    override fun onDestroyView() {
        super.onDestroyView()
        signUpFragment = null
    }
}
