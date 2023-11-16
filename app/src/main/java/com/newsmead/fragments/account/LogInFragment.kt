package com.newsmead.fragments.account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.newsmead.activities.MainActivity
import com.newsmead.R

import com.newsmead.databinding.FragmentLogInBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewBinding: FragmentLogInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Initialize viewBinding for LogInFragment
        this.viewBinding = FragmentLogInBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        this.auth = FirebaseAuth.getInstance()

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

            if (checkAccountErrors(email, password, "")) {
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
                        Toast.makeText(requireActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        this.viewBinding.cbViewPassword.setOnClickListener {
            val cursorPosition = viewBinding.etAccLogPassword.selectionStart
            this.viewBinding.etAccLogPassword.transformationMethod = if (this.viewBinding.cbViewPassword.isChecked) null else android.text.method.PasswordTransformationMethod()
            this.viewBinding.etAccLogPassword.setSelection(cursorPosition)
        }

        // Inflate the layout for this fragment
        return this.viewBinding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    /**
     * Goes to MainActivity while clearing all other activities
     */
    private fun successfulLogIn() {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

    /**
     * Provides all error checking for email, password, and confirm password
     * @param email The email entered by the user
     * @param password The password entered by the user
     * @param confirmPassword The confirm password entered by the user
     */
    private fun checkAccountErrors(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty()) {
            this.viewBinding.etAccLogEmail.error = "Email is required"
            this.viewBinding.etAccLogEmail.requestFocus()
            return true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.viewBinding.etAccLogEmail.error = "Please provide valid email"
            this.viewBinding.etAccLogEmail.requestFocus()
            return true
        }

        if (password.isEmpty()) {
            this.viewBinding.etAccLogPassword.error = "Password is required"
            this.viewBinding.etAccLogPassword.requestFocus()
            return true
        } else if (password.length < 6) {
            this.viewBinding.etAccLogPassword.error = "Min password length should be 6 characters"
            this.viewBinding.etAccLogPassword.requestFocus()
            return true
        }

        return false
    }
}