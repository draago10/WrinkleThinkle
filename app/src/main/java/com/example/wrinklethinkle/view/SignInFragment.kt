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
import com.example.wrinklethinkle.Utility.Utility
import com.example.wrinklethinkle.viewmodel.SignInScreenViewModel
import com.example.wrinklethinkle.databinding.FragmentSignInScreenBinding
import com.example.wrinklethinkle.viewmodel.SignUpScreenViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {
    private var signInFragment: FragmentSignInScreenBinding? = null
    private val binding get() = signInFragment!!

    private val viewModel: SignInScreenViewModel by viewModels()
    private  lateinit var auth: FirebaseAuth


    var emailText = ""
    var passwordText = ""
    var errorMessage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {
        // Inflate the layout for this fragment
        signInFragment = FragmentSignInScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()
        val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).build()

        viewModel.signInResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                val navController = findNavController()
                val navOptions = NavOptions.Builder().setEnterAnim(R.anim.slide_up).build()
                navController.navigate(R.id.action_signInFragment_to_appStartFrament, null, navOptions)
                //Navigate to app start screen
            } else {
                //Toast.makeText(context, "WE LOSE", Toast.LENGTH_SHORT).show()
                Utility().showErrorPopup(childFragmentManager, requireContext(), "Oops, something went wrong...", errorMessage, { poop() })
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error.let {
                errorMessage = it
            }
        }

        binding.signInScreenSignupButton.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment, null, navOptions)
        }

        //test5@gmail.com 12364567
        binding.signInScreenSigninButton.setOnClickListener {
            if (binding.signInScreenEmail.text.toString().isEmpty() || binding.signInScreenPassword.text.toString().isEmpty()) {
                Utility().showErrorPopup(childFragmentManager, requireContext(), "Oops, empty fields detected!", "Please do not leave the fields empty.", {poop()})
            } else {
                emailText = binding.signInScreenEmail.text.toString().trim()
                passwordText = binding.signInScreenPassword.text.toString().trim()
                viewModel.signInUser(emailText, passwordText)
            }
        }

    }

    fun poop() {

    }


}