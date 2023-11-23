package com.newsmead.fragments.layouts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.CollectionReference
import com.newsmead.data.DataHelper
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.BottomSheetDialogSaveListBinding
import com.newsmead.models.SavedList
import com.newsmead.recyclerviews.dialog.SheetDialogAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BottomSheetDialogSaveFragment(private val articleId: String): BottomSheetDialogFragment(), listListener {

    private lateinit var binding: BottomSheetDialogSaveListBinding
    private lateinit var lists: ArrayList<SavedList>
    private var checkedLists: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = BottomSheetDialogSaveListBinding.inflate(inflater, container, false)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                lists = FirebaseHelper.getListsCollection(requireContext()) ?: return@launch

                // Read later is always first, no need to sort

                // Update UI on the main thread
                withContext(Dispatchers.Main) {
                    binding.rvDialogList.adapter = SheetDialogAdapter(lists, this@BottomSheetDialogSaveFragment)
                    val layoutManager = LinearLayoutManager(context)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    binding.rvDialogList.layoutManager = layoutManager
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error getting lists", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }
        }

        binding.btnNewList.setOnClickListener {
            showNewListDialog()
        }

        binding.btnListDone.setOnClickListener {
            Log.d("BottomSheetDialogSaveFragment", "Saving List...")
            Log.d("BottomSheetDialogSaveFragment", "Checked lists: $checkedLists")
            // Save details here
            if (checkedLists.isEmpty()) {
                Toast.makeText(context, "No list selected", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("BottomSheetDialogSaveFragment", "Saving article to lists...")
                // Save article to checked lists

                for (listId in checkedLists) {
                    Log.d("BottomSheetDialogSaveFragment", "Saving article $articleId to list $listId")
                    FirebaseHelper.addArticleToFireStoreList(
                        requireContext(), listId, articleId
                    )
                }

                Toast.makeText(context, "Article saved to ${checkedLists.size} lists", Toast.LENGTH_SHORT).show()
            }

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