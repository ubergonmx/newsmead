package com.newsmead.fragments.layouts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.newsmead.data.DataHelper.loadListNamesData
import com.newsmead.databinding.BottomSheetDialogSaveListBinding
import com.newsmead.recyclerviews.dialog.SheetDialogAdapter

class BottomSheetDialogSaveFragment: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogSaveListBinding
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var lists: CollectionReference
    private var listsId: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = BottomSheetDialogSaveListBinding.inflate(inflater, container, false)

        // Supply data of lists from Firestore
//        val data = DataHelper.loadListNamesData()

        this.lists = firestore.collection("users").document(
            FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("lists")

        val data = ArrayList<String>()

        // Add list names to data
        lists.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    data.add(document.data["name"].toString())

                    // Add list id to listsId
                    listsId.add(document.id)

//                    Log.d("List Added", "${document.id} => ${document.data["name"]}")
                }

                // Mount recyclerView here
                binding.rvDialogList.adapter = SheetDialogAdapter(data)
                val layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                binding.rvDialogList.layoutManager = layoutManager
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error getting lists", Toast.LENGTH_SHORT).show()

                // Else mount data from DataHelper
                // Mount recyclerView here
                data += loadListNamesData()
                binding.rvDialogList.adapter = SheetDialogAdapter(data)
                val layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                binding.rvDialogList.layoutManager = layoutManager
            }
        
        binding.btnNewList.setOnClickListener {
            showNewListDialog()
        }

        binding.btnListDone.setOnClickListener {
            // Save details here

            // Check which lists are selected
            val selectedLists = mutableListOf<String>()
            val adapter = binding.rvDialogList.adapter as SheetDialogAdapter

            dismiss()
        }

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