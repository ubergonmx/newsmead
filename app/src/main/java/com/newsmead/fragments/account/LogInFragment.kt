package com.newsmead.fragments.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.newsmead.activities.MainActivity
import com.newsmead.R
import com.newsmead.data.FirebaseHelper
import com.newsmead.data.PreloadedData

import com.newsmead.databinding.FragmentLogInBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    private lateinit var viewBinding: FragmentLogInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Initialize viewBinding for LogInFragment
        this.viewBinding = FragmentLogInBinding.inflate(inflater, container, false)
        this.auth = FirebaseAuth.getInstance()
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )

        // Listener for text fields to clear error
        this.viewBinding.etAccLogEmail.setOnFocusChangeListener { _, _ ->
            this.viewBinding.tilAccLogEmail.error = null
        }

        this.viewBinding.etAccLogPassword.setOnFocusChangeListener { _, _ ->
            this.viewBinding.tilAccLogPassword.error = null
        }


        // Buttons
        this.viewBinding.btnAccCreate.setOnClickListener {
            val signUpFragment = SignUpFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.flAccountContainer, signUpFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        this.viewBinding.btnAccLogIn.setOnClickListener {

            // Log In Validation
            val email = this.viewBinding.etAccLogEmail.text.toString()
            val password = this.viewBinding.etAccLogPassword.text.toString()

            if (checkAccountErrors(email, password)) {
                return@setOnClickListener
            }

            // Log In User
            this.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        successfulLogIn()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(requireActivity(), "Authentication failed. Wrong email or password.", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        // Inflate the layout for this fragment
        return this.viewBinding.root
    }

    /**
     * Goes to MainActivity while clearing all other activities
     */
    private fun successfulLogIn() {
        lifecycleScope.launch {
            // Load preloaded data
            val pairData = FirebaseHelper.getListsAndArticles(requireActivity())
            PreloadedData.updateSavedData(pairData)

            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
    }

    /**
     * Provides all error checking for email, password, and confirm password
     * @param email The email entered by the user
     * @param password The password entered by the user
     */
    private fun checkAccountErrors(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            this.viewBinding.tilAccLogEmail.error = "Email is required"
            return true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.viewBinding.tilAccLogEmail.error = "Please provide valid email"
            return true
        }

        if (password.isEmpty()) {
            this.viewBinding.tilAccLogPassword.error = "Password is required"
            return true
        } else if (password.length < 6) {
            this.viewBinding.tilAccLogPassword.error = "Min password length should be 6 characters"
            return true
        }

        return false
    }
}