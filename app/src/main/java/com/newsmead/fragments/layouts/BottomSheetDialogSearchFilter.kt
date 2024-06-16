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
    private var language: String = ""
    private var sortBy: String = ""
    private var startDate: String = ""
    private var endDate: String = ""
    interface OnApplyFiltersListener {
            fun onApplyFilters(category: String?, source: String, language: String, sortBy: String, startDate: String, endDate: String)
    }

    var onApplyFiltersListener: OnApplyFiltersListener? = null

    companion object {
        fun newInstance(category: String?, source: String, language: String, sortBy: String, startDate: String, endDate: String): BottomSheetDialogSearchFilter {
            val fragment = BottomSheetDialogSearchFilter()
            val args = Bundle()
            args.putString("category", category)
            args.putString("source", source)
            args.putString("language", language)
            args.putString("sortBy", sortBy)
            args.putString("startDate", startDate)
            args.putString("endDate", endDate)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            category = arguments?.getString("category")
            source = arguments?.getString("source") ?: ""
            language = arguments?.getString("language") ?: ""
            sortBy = arguments?.getString("sortBy") ?: ""
            startDate = arguments?.getString("startDate") ?: ""
            endDate = arguments?.getString("endDate") ?: ""
        }
    }
    


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDialogSearchFilterBinding.inflate(inflater, container, false)

        val categoryData = DataHelper.loadCategoryData()
        val sourcesData = DataHelper.loadSourcesData()
        val languagesData = DataHelper.loadLanguagesData()

        // Fill chips
        for (category in categoryData) {
            val chipBinding = ChipSearchBinding.inflate(inflater, container, false).root
            chipBinding.text = category
            if (category == this.category) {
                chipBinding.isChecked = true
            }
            binding.cgDialogCategory.addView(chipBinding)

            chipBinding.setOnClickListener {
                updateToCategory(category)
            }
        }

        for (source in sourcesData) {
            val chipBinding = ChipSearchBinding.inflate(inflater, container, false).root
            chipBinding.text = source
            if (source == this.source) {
                chipBinding.isChecked = true
            }
            binding.cgDialogSource.addView(chipBinding)

            chipBinding.setOnClickListener {
                updateToSource(source)
            }
        }

        for (language in languagesData) {
            val chipBinding = ChipSearchBinding.inflate(inflater, container, false).root
            chipBinding.text = language
            if (language == this.language) {
                chipBinding.isChecked = true
            }
            binding.cgDialogLanguage.addView(chipBinding)

            chipBinding.setOnClickListener {
                updateToLanguage(language)
            }
        }

        // Set default checked to most relevant
        binding.radioGroup.check(binding.rbRecent.id)

        // Date Picker
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select a date range")
            .build()

        // Set the date range if it was previously selected
        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            // Create a SimpleDateFormat object for reading the date in the given format
            val sdfSource = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

            // Parse the start and end dates into Date objects
            val start = sdfSource.parse(startDate)
            val end = sdfSource.parse(endDate)

            // Create a SimpleDateFormat object for formatting the date as you want
            val sdfDestination = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)

            // Format the start and end dates into the new format
            val formattedStart = sdfDestination.format(start)
            val formattedEnd = sdfDestination.format(end)

            // Set the formatted date range on the button
            binding.btnDateFilter.text = "Date: $formattedStart — $formattedEnd"
        }

        // Date Picker Listener
        dateRangePicker.addOnPositiveButtonClickListener {
            // Get the selected date range
            val dateRange = dateRangePicker.selection

            if (dateRange != null) {
                // Convert the dates from milliseconds to a readable format
                startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateRange.first))
                endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(dateRange.second))

                // Use the dates here
                binding.btnDateFilter.text = "Date: ${dateRangePicker.headerText}"
            }
        }

        binding.btnDateFilter.setOnClickListener {
            dateRangePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        binding.btnSearchFiltersApply.setOnClickListener {
            onApplyFiltersListener?.onApplyFilters(this.category, this.source, this.language, this.sortBy, this.startDate, this.endDate)
            dismiss()
        }

        return binding.root
    }

    /**
     * Select only one chip from a group
     * @param selected The selected chip
     * @param group The group of chips (0 = category, 1 = source, 2 = language)
     */
    private fun selectOnlyOneChip(selected:String, group:Int){
        val chipGroup = when(group){
            0 -> binding.cgDialogCategory
            1 -> binding.cgDialogSource
            2 -> binding.cgDialogLanguage
            else -> return
        }

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.text != selected) {
                chip.isChecked = false
            }
        }

        when(group){
            0 -> category = selected
            1 -> source = selected
            2 -> language = selected
        }
    }

    private fun updateToCategory(category: String) {
        selectOnlyOneChip(category, 0)
    }

    private fun updateToSource(source: String) {
        selectOnlyOneChip(source, 1)
    }

    private fun updateToLanguage(language: String) {
        selectOnlyOneChip(language, 2)
    }
}