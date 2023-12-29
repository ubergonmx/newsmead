package com.newsmead.fragments.account

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.newsmead.R
import com.newsmead.data.FirebaseHelper

import com.newsmead.databinding.FragmentSignUpBinding

class SignUpFragment: Fragment() {
    private lateinit var viewBinding: FragmentSignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.viewBinding = FragmentSignUpBinding.inflate(inflater, container, false)
        this.auth = FirebaseAuth.getInstance()

        // Textfields to erase error messages
        this.viewBinding.etAccEmail.setOnFocusChangeListener { _, _ ->
            this.viewBinding.etAccEmail.error = null
        }

        this.viewBinding.etAccPassword.setOnFocusChangeListener { _, _ ->
            this.viewBinding.tilAccPassword.error = null
        }

        this.viewBinding.etAccConfirmPassword.setOnFocusChangeListener { _, _ ->
            this.viewBinding.tilAccConfirmPassword.error = null
        }

        // Buttons
        this.viewBinding.btnAccLog.setOnClickListener {
            // Goes to Log In Fragment
            val logInFragment = LogInFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flAccountContainer, logInFragment)
                .commit()
        }

        this.viewBinding.btnAccStart.setOnClickListener {
            // Temporarily disable button
            this.viewBinding.btnAccStart.isEnabled = false

            val name = this.viewBinding.etAccName.text.toString()
            val email = this.viewBinding.etAccEmail.text.toString()
            val password = this.viewBinding.etAccPassword.text.toString()
            val confirmPassword = this.viewBinding.etAccConfirmPassword.text.toString()

            if (checkAccountErrors(email, password, confirmPassword)) {
                // Re-enable button
                this.viewBinding.btnAccStart.isEnabled = true
                return@setOnClickListener
            }

            // Create user with email and password
            this.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = this.auth.currentUser
                        user!!.sendEmailVerification()
                            .addOnCompleteListener { taskEmail ->
                                if (taskEmail.isSuccessful) {
                                    // Add user to Firestore and then go to Onboarding Fragment once added
                                    FirebaseHelper.addUserToFirestore(requireActivity(), email, name)
                                    finishSignUp()
                                }
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                         Log.w(TAG, "createUserWithEmail:failure", task.exception)
                         Toast.makeText(requireActivity(), "Authentication failed. Please try again.",
                             Toast.LENGTH_SHORT).show()

                        // Re-enable button
                        this.viewBinding.btnAccStart.isEnabled = true
                    }
                }
        }

        return this.viewBinding.root
    }

    /**
     * Goes to Onboarding Fragment
     */
    private fun finishSignUp() {
        // Goes to Onboarding Fragment
        val onboardingFragment = OnboardingFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flAccountContainer, onboardingFragment)
            .commit()
    }

    /**
     * Provides all error checking for email, password, and confirm password
     * @param email The email entered by the user
     * @param password The password entered by the user
     * @param confirmPassword The confirm password entered by the user
     */
    private fun checkAccountErrors(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            this.viewBinding.etAccEmail.error = "Email is required"
            return true
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.viewBinding.etAccEmail.error = "Please provide valid email"
            return true
        }

        if (password.isEmpty()) {
            this.viewBinding.tilAccPassword.error = "Password is required"
            return true
        } else if (password.length < 6) {
            this.viewBinding.tilAccPassword.error = "Min password length should be 6 characters"
            return true
        }

        if (confirmPassword.isEmpty()) {
            // MUI Error
            this.viewBinding.tilAccConfirmPassword.error = "Confirm Password is required"
            return true
        } else if (password != confirmPassword) {
            this.viewBinding.tilAccConfirmPassword.error = "Password and Confirm Password does not match"
            return true
        }

        return false
    }

    private fun clearTextFields() {
        this.viewBinding.etAccEmail.setText("")
        this.viewBinding.etAccPassword.setText("")
        this.viewBinding.etAccConfirmPassword.setText("")
        this.viewBinding.etAccEmail.clearFocus()
        this.viewBinding.etAccPassword.clearFocus()
        this.viewBinding.etAccConfirmPassword.clearFocus()
        this.viewBinding.etAccEmail.error = null
        this.viewBinding.etAccPassword.error = null
        this.viewBinding.etAccConfirmPassword.error = null
    }
}