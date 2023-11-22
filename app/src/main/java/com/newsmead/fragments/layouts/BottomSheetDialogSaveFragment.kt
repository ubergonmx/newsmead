package com.newsmead.fragments.layouts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.newsmead.data.DataHelper
import com.newsmead.data.DataHelper.loadListNamesData
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.BottomSheetDialogSaveListBinding
import com.newsmead.models.SavedList
import com.newsmead.recyclerviews.dialog.SheetDialogAdapter

class BottomSheetDialogSaveFragment: BottomSheetDialogFragment(), listListener {

    private lateinit var binding: BottomSheetDialogSaveListBinding
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var lists: CollectionReference
    private var checkedLists: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = BottomSheetDialogSaveListBinding.inflate(inflater, container, false)

        // Supply data of lists from Firestore
//        val data = DataHelper.loadListNamesData()

        this.lists = FirebaseHelper.getListsCollection(requireContext()) ?: return binding.root

        val data = ArrayList<SavedList>()

        // Add list names to data
        lists.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val listId = document.id
                    val listName = document.data["name"].toString()

                    data.add(SavedList(listId, listName))
                }

                // Place the read later list at the top
                val readLaterList = data.find { it.title == "Read Later" }
                if (readLaterList != null) {
                    data.remove(readLaterList)
                    data.add(0, readLaterList)
                }

                // Mount recyclerView here
                binding.rvDialogList.adapter = SheetDialogAdapter(data, this)
                val layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                binding.rvDialogList.layoutManager = layoutManager
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Error getting lists", Toast.LENGTH_SHORT).show()

                // Else mount data from DataHelper
                // Mount recyclerView here
                data += DataHelper.loadListData()
                binding.rvDialogList.adapter = SheetDialogAdapter(data, this)
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
                val newList = SavedList(result, newListName)
                adapter.addNewList(newList)
            }
        }
        newListDialog.show()
    }

    /**
     * Called when a list is checked or unchecked
     * @param listId Id of the list that was checked or unchecked
     */
    override fun onListChecked(listId: String) {
        // Add or remove listId from checkedLists
        if (checkedLists.contains(listId)) {
            checkedLists.remove(listId)
        } else {
            checkedLists.add(listId)
        }

        Log.d("BottomSheetDialogSaveFragment", "Checked lists: $checkedLists")
    }
}