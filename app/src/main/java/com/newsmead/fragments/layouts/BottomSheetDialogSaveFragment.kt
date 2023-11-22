package com.newsmead.fragments.layouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.newsmead.data.DataHelper.loadListNamesData
import com.newsmead.data.FirebaseHelper
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

        this.lists = FirebaseHelper.getListsCollection(requireContext()) ?: return binding.root

        val data = ArrayList<String>()

        // Add list names to data
        lists.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    data.add(document.data["name"].toString())

                    // Add list id to listsId
                    listsId.add(document.id)
                }

                // Place the read later list at the top
                val readLaterIndex = data.indexOf("Read Later")
                if (readLaterIndex != -1) {
                    val readLater = data[readLaterIndex]
                    data.removeAt(readLaterIndex)
                    data.add(0, readLater)
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
            val result = FirebaseHelper.addListToFireStore(requireContext(), newListName)

            if (result != "") {
                // Add new list to recyclerView
                val adapter = binding.rvDialogList.adapter as SheetDialogAdapter
                adapter.addNewList(newListName)

                // Add new list id to listsId
                listsId.add(result)
            }
        }
        newListDialog.show()
    }
}