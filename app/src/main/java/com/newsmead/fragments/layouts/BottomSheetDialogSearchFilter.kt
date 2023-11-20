package com.newsmead.fragments.layouts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.newsmead.data.DataHelper

import com.newsmead.databinding.BottomSheetDialogSearchFilterBinding
import com.newsmead.databinding.ChipSearchBinding

class BottomSheetDialogSearchFilter: BottomSheetDialogFragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetDialogSearchFilterBinding.inflate(inflater, container, false)

        val categoryData = DataHelper.loadCategoryData()
        val sourcesData = DataHelper.loadSourcesData()

        // Fill chips
        for (category in categoryData) {
            val chipBinding = ChipSearchBinding.inflate(inflater, container, false).root
            chipBinding.text = category
            binding.cgDialogCategory.addView(chipBinding)
        }

        for (source in sourcesData) {
            val chipBinding = ChipSearchBinding.inflate(inflater, container, false).root
            chipBinding.text = source
            binding.cgDialogSource.addView(chipBinding)
        }

        // Set default checked to most relevant
        binding.radioGroup.check(0)

        // Date Picker
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select a date range")
            .build()

        // Rename button text
        dateRangePicker.addOnPositiveButtonClickListener {
            binding.btnDateFilter.text = "Date: ${dateRangePicker.headerText}"
        }

        binding.btnDateFilter.setOnClickListener {
            dateRangePicker.show(parentFragmentManager, "DATE_PICKER")


        }


        return binding.root
    }
}