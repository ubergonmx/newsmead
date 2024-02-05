package com.newsmead.fragments.layouts

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.newsmead.data.DataHelper

import com.newsmead.databinding.BottomSheetDialogSearchFilterBinding
import com.newsmead.databinding.ChipSearchBinding
import java.util.Date
import java.util.Locale

class BottomSheetDialogSearchFilter: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogSearchFilterBinding
    private var category: String? = "All"
    private var source: String = ""
    private var sortBy: String = ""
    private var startDate: String = ""
    private var endDate: String = ""
    interface OnApplyFiltersListener {
            fun onApplyFilters(category: String?, source: String, sortBy: String, startDate: String, endDate: String)
    }

    var onApplyFiltersListener: OnApplyFiltersListener? = null


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDialogSearchFilterBinding.inflate(inflater, container, false)

        val categoryData = DataHelper.loadCategoryData()
        val sourcesData = DataHelper.loadSourcesData()

        // Fill chips
        for (category in categoryData) {
            val chipBinding = ChipSearchBinding.inflate(inflater, container, false).root
            chipBinding.text = category
            binding.cgDialogCategory.addView(chipBinding)

            chipBinding.setOnClickListener {
                updateToCategory(category)
            }
        }

        for (source in sourcesData) {
            val chipBinding = ChipSearchBinding.inflate(inflater, container, false).root
            chipBinding.text = source
            binding.cgDialogSource.addView(chipBinding)

            chipBinding.setOnClickListener {
                updateToSource(source)
            }
        }

        // Set default checked to most relevant
        binding.radioGroup.check(0)

        // Date Picker
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select a date range")
            .build()

        // Rename button text
        dateRangePicker.addOnPositiveButtonClickListener {
            // Get the selected date range
            val dateRange = dateRangePicker.selection

            if (dateRange != null) {
                // Convert the dates from milliseconds to a readable format
                startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateRange.first))
                endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateRange.second))

                // Use the dates here
                binding.btnDateFilter.text = "Date: $startDate - $endDate"
            }
        }

        binding.btnDateFilter.setOnClickListener {
            dateRangePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        binding.btnSearchFiltersApply.setOnClickListener {
            onApplyFiltersListener?.onApplyFilters(this.category, this.source, this.sortBy, this.startDate, this.endDate)
            dismiss()
        }

        return binding.root
    }

    private fun updateToCategory(category: String) {
        // Uncheck all chips except for the one that was clicked
        for (i in 0 until binding.cgDialogCategory.childCount) {
            val chip = binding.cgDialogCategory.getChildAt(i) as Chip
            if (chip.text != category) {
                chip.isChecked = false
            }
        }

        this.category = category
    }

    private fun updateToSource(source: String) {
        // Uncheck all chips except for the one that was clicked
        for (i in 0 until binding.cgDialogSource.childCount) {
            val chip = binding.cgDialogSource.getChildAt(i) as Chip
            if (chip.text != source) {
                chip.isChecked = false
            }
        }

        this.source = source
    }
}