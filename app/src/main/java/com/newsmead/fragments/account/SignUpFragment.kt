package com.newsmead.fragments.account

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.newsmead.R

import com.newsmead.databinding.FragmentSignUpBinding

class SignUpFragment: Fragment() {
    private lateinit var viewBinding: FragmentSignUpBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.viewBinding = FragmentSignUpBinding.inflate(inflater, container, false)

        // Buttons
        this.viewBinding.btnAccLog.setOnClickListener {
            // Goes to Log In Fragment
            val logInFragment = LogInFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flAccountContainer, logInFragment)
                .commit()
        }

        this.viewBinding.btnAccStart.setOnClickListener {
            // Goes to Onboarding Fragment
//            val onboardingFragment = OnboardingFragment()
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.flAccountContainer, onboardingFragment)
//                .commit()

            val email = this.viewBinding.etAccEmail.text.toString()
            val password = this.viewBinding.etAccPassword.text.toString()
            val confirmPassword = this.viewBinding.etAccConfirmPassword.text.toString()

            if (email.isEmpty()) {
                this.viewBinding.etAccEmail.error = "Email is required"
                this.viewBinding.etAccEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                this.viewBinding.etAccEmail.error = "Please provide valid email"
                this.viewBinding.etAccEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                this.viewBinding.etAccPassword.error = "Password is required"
                this.viewBinding.etAccPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 4) {
                this.viewBinding.etAccPassword.error = "Min password length should be 4 characters"
                this.viewBinding.etAccPassword.requestFocus()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                this.viewBinding.etAccConfirmPassword.setError("Confirm Password is required")
                this.viewBinding.etAccConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (!password.equals(confirmPassword)) {
                this.viewBinding.etAccConfirmPassword.setError("Password and Confirm Password does not match")
                this.viewBinding.etAccConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // Create user with email and password
        }

        this.viewBinding.cbViewPassword.setOnClickListener {
            val cursorPosition = this.viewBinding.etAccPassword.selectionStart
            this.viewBinding.etAccPassword.transformationMethod = if (this.viewBinding.cbViewPassword.isChecked) null else android.text.method.PasswordTransformationMethod()
            this.viewBinding.etAccPassword.setSelection(cursorPosition)
        }

        this.viewBinding.cbViewConfirmPassword.setOnClickListener {
            val cursorPosition = this.viewBinding.etAccConfirmPassword.selectionStart
            this.viewBinding.etAccConfirmPassword.transformationMethod = if (this.viewBinding.cbViewConfirmPassword.isChecked) null else android.text.method.PasswordTransformationMethod()
            this.viewBinding.etAccConfirmPassword.setSelection(cursorPosition)
        }

        return this.viewBinding.root
    }
}