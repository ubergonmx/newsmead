package com.newsmead.fragments.account

import android.os.Bundle
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
            val onboardingFragment = OnboardingFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flAccountContainer, onboardingFragment)
                .commit()
        }



        return this.viewBinding.root
    }
}