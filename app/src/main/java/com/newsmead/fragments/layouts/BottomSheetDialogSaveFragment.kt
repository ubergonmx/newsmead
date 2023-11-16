package com.newsmead.fragments.layouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.newsmead.R
import com.newsmead.data.DataHelper
import com.newsmead.databinding.BottomSheetDialogSaveListBinding
import com.newsmead.recyclerviews.dialog.SheetDialogAdapter

class BottomSheetDialogSaveFragment: BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = BottomSheetDialogSaveListBinding.inflate(inflater, container, false)

        binding.btnNewList.setOnClickListener {
            showNewListDialog()
        }

        binding.btnListDone.setOnClickListener {
            // Save details here

            dismiss()
        }

        // Mount recyclerView here
        val data = DataHelper.loadListNamesData()
        binding.rvDialogList.adapter = SheetDialogAdapter(data)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvDialogList.layoutManager = layoutManager

        return binding.root
    }

    private fun showNewListDialog() {
        val newListDialog = NewListDialog(requireContext()) { newListName ->
            // Add new list to database
        }
        newListDialog.show()
    }
}