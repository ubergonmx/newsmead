package com.newsmead.fragments.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import com.newsmead.DataHelper.loadCategoryLongerData
import com.newsmead.MainActivity
import com.newsmead.R

import com.newsmead.databinding.FragmentOnboardingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var checkedChips: List<Int>

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
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        val chipData = loadCategoryLongerData()
        val chipGroup = binding.cgCategoryTopics

        checkedChips = listOf()

        for (category in chipData) {
            val chip = layoutInflater.inflate(R.layout.chip_search, chipGroup, false) as Chip
            chip.text = category

            // Make chips toggleable
            chip.isCheckable = true

            // Add listener to enable button when 3 categories are selected
            chip.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    checkedChips += buttonView.id
                } else {
                    checkedChips -= buttonView.id
                }

                if (checkedChips.size >= 3) {
                    binding.btnFinishOnboarding.isEnabled = true
                    binding.btnFinishOnboarding.alpha = 1.0f
                } else {
                    binding.btnFinishOnboarding.isEnabled = false
                    binding.btnFinishOnboarding.alpha = 0.5f
                }
            }

            chipGroup.addView(chip)
        }

        // Disable button if less than 3 categories are selected
        binding.btnFinishOnboarding.isEnabled = false
        binding.btnFinishOnboarding.alpha = 0.5f

        // Button for onboarding
        binding.btnFinishOnboarding.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OnboardingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OnboardingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}