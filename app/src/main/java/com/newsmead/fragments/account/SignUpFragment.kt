package com.newsmead.fragments.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.newsmead.databinding.FragmentSignupBinding

class SignUpFragment: Fragment() {
    private lateinit var viewBinding: FragmentSignupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.viewBinding = FragmentSignupBinding.inflate(inflater, container, false)

        // Buttons
        this.viewBinding.btnAccLog.setOnClickListener {
            // Goes to Log In Fragment
            val logInFragment = LogInFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(com.newsmead.R.id.flAccountContainer, logInFragment)
                .commit()
        }

        

        return this.viewBinding.root
    }
}