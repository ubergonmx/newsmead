package com.newsmead.fragments.layouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.newsmead.data.DataHelper
import com.newsmead.databinding.BottomSheetDialogSaveListBinding
import com.newsmead.recyclerviews.dialog.SheetDialogAdapter

class BottomSheetDialogSaveFragment: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogSaveListBinding
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = BottomSheetDialogSaveListBinding.inflate(inflater, container, false)

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
            addListToFireStore(newListName)
        }
        newListDialog.show()
    }

    /**
     * Adds a new list to Firestore
     * @param newListName Name of the new list
     */
    private fun addListToFireStore(newListName: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            Toast.makeText(context, "Error creating list", Toast.LENGTH_SHORT).show()
            return
        }

        val userListsRef = firestore.collection("users").document(uid).collection("lists")
        val newListId = userListsRef.document().id

        // Create new list document
        userListsRef.document(newListId).set(
            hashMapOf(
                "name" to newListName
            )
        )
        .addOnSuccessListener {
            // Handle Success
            Toast.makeText(context, "List created", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            // Handle Failure
            Toast.makeText(context, "Error creating list", Toast.LENGTH_SHORT).show()
        }
    }
}