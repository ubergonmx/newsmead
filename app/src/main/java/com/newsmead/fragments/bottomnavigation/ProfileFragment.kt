package com.newsmead.fragments.bottomnavigation

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.newsmead.activities.AccountActivity
import com.newsmead.data.PreloadedData

// ViewBinding
import com.newsmead.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var viewBinding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize viewBinding for ProfileFragment
        this.viewBinding = FragmentProfileBinding.inflate(layoutInflater)

        // Connect to Firebase Auth
        this.auth = FirebaseAuth.getInstance()

        // Fill Email Field
        this.viewBinding.etEmail.setText(this.auth.currentUser?.email)

        // Buttons
        this.viewBinding.btnLogout.setOnClickListener {
            this.auth.signOut()
            PreloadedData.clearData()

            // Finish Activity and Go to AccountActivity and reset the back stack
            val intent = Intent(requireActivity(), AccountActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

        this.viewBinding.btnSave.setOnClickListener {
//            val name = this.viewBinding.etName.text.toString()
            val password = this.viewBinding.etPassword.text.toString()
            val confirmPassword = this.viewBinding.etConfirmPassword.text.toString()

            updateProfile("User", password, confirmPassword)
        }

        // Check if dark mode is enabled on phone
        val activityConfig = requireActivity().resources.configuration.uiMode
        if (activityConfig and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is enabled
            this.viewBinding.swDarkmode.isChecked = true
        }

        // Night mode switch
        this.viewBinding.swDarkmode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Switch to dark mode through
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                // Switch to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return this.viewBinding.root
    }

    private fun updateProfile(name: String, password: String, confirmPassword: String): Boolean {
        var result = false
        if (checkPasswordError(password, confirmPassword)) {
            return false
        } else {
            // Firebase updates password of user
            this.auth.currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Password updated successfully
                    resetTextFields()
                    result = true
                } else {
                    // Password update failed
                    this.viewBinding.etPassword.error = task.exception?.message
                    this.viewBinding.etPassword.requestFocus()
                    result = false
                }
            }
        }

        return result
    }

    /**
     * Provides all error checking for password and confirm password
     * @param password The password entered by the user
     * @param confirmPassword The confirm password entered by the user
     */
    private fun checkPasswordError(password: String, confirmPassword: String): Boolean {
        if (password.isEmpty() && confirmPassword.isEmpty()) {
            return false
        } else if (password.isEmpty()) {
                this.viewBinding.etPassword.error = "Password is required"
                this.viewBinding.etPassword.requestFocus()
                return true
        } else if (password.length < 6) {
            this.viewBinding.etPassword.error = "Min password length should be 6 characters"
            this.viewBinding.etPassword.requestFocus()
            return true
        } else if (confirmPassword.isEmpty()) {
            this.viewBinding.etConfirmPassword.error = "Confirm Password is required"
            this.viewBinding.etConfirmPassword.requestFocus()
            return true
        } else if (password != confirmPassword) {
            this.viewBinding.etConfirmPassword.error = "Password and Confirm Password does not match"
            this.viewBinding.etConfirmPassword.requestFocus()
            return true
        }
        return false
    }

    private fun resetTextFields() {
        this.viewBinding.etPassword.setText("")
        this.viewBinding.etConfirmPassword.setText("")
        this.viewBinding.etPassword.clearFocus()
        this.viewBinding.etConfirmPassword.clearFocus()
        this.viewBinding.etPassword.error = null
        this.viewBinding.etConfirmPassword.error = null
    }
}