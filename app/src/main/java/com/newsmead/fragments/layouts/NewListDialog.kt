package com.newsmead.fragments.layouts

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

// View Binding
import com.newsmead.databinding.DialogNewListBinding

class NewListDialog(context: Context, private val onListCreated: (String) -> Unit) {

    private val builder = AlertDialog.Builder(context)

    // View Binding
    private val binding = DialogNewListBinding.inflate(LayoutInflater.from(context))
    private val view = binding.root

    init {
        val etListName = binding.etNewList

        builder.setView(view)
            .setTitle("Create New List")
            .setPositiveButton("Create") { dialog, _ ->
                val newListName = etListName.text.toString()
                onListCreated.invoke(newListName)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
    }

    fun show() {
        builder.create().show()
    }
}