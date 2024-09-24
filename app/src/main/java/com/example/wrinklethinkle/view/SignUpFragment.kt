package com.example.wrinklethinkle.view

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.wrinklethinkle.databinding.FragmentSignUpBinding
import com.example.wrinklethinkle.viewmodel.SignUpViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class SignUpFragment : Fragment() {

    private var signUpFragment: FragmentSignUpBinding? = null
    private val binding get() = signUpFragment!!

    private val viewModel: SignUpViewModel by viewModels()
    private  lateinit var auth: FirebaseAuth
    var email = ""
    var password = ""

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
                Toast.makeText(context, "WE WIN", Toast.LENGTH_SHORT).show()
                //Navigate to app start screen
            } else {
                Toast.makeText(context, "WE LOSE", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signUpScreenSignupButton.setOnClickListener {
            email = binding.signUpScreenEmail.text.toString().trim()
            password = binding.signUpScreenPassword.text.toString().trim()
            //ERROR HANDLING
            viewModel.createUser(email, password)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        signUpFragment = null
    }
}