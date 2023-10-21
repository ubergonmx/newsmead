package com.newsmead.fragments.layouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.newsmead.DataHelper

import com.newsmead.databinding.BottomSheetDialogSearchFilterBinding
import com.newsmead.databinding.ChipSearchBinding

class BottomSheetDialogSearchFilter: BottomSheetDialogFragment() {
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



        return binding.root
    }
}