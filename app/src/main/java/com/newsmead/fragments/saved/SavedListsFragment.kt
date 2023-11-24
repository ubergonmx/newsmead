package com.newsmead.fragments.saved
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.newsmead.data.DataHelper.loadListData
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.FragmentSavedYourListsBinding
import com.newsmead.fragments.bottomnavigation.SavedFragmentDirections
import com.newsmead.fragments.layouts.NewListDialog
import com.newsmead.models.Article
import com.newsmead.models.SavedList
import com.newsmead.recyclerviews.dialog.SheetDialogAdapter
import com.newsmead.recyclerviews.saved.SavedListsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SavedListsFragment: Fragment(), cardListener {
    private lateinit var binding: FragmentSavedYourListsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var data: ArrayList<SavedList>
    private lateinit var adapter: SavedListsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedYourListsBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Initialize recyclerView immediately
        data = ArrayList()
        adapter = SavedListsAdapter(ArrayList(), this)
        binding.rvSavedLists.adapter = adapter

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSavedLists.layoutManager = layoutManager

        // Retrieve lists from Firestore using coroutines
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listsCollection = FirebaseHelper.getListsCollection(requireContext())
                if (listsCollection != null) {
                    // ListsCollection is not null, fetch the data
                    // Read later is always first, no need to sort
                    data = listsCollection

                    withContext(Dispatchers.Main) {
                        adapter.updateData(data)
                    }
                }
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Button for Create New List
        binding.btnCreateList.setOnClickListener {
            val newListDialog = NewListDialog(requireContext()) { newListName ->
                // Add new list to database
                val result = FirebaseHelper.addListToFireStore(requireContext(), newListName)

                if (result != "") {
                    // Add new list to recyclerView
                    val adapter = binding.rvSavedLists.adapter as SheetDialogAdapter
                    val newList = SavedList(result, newListName)
                    adapter.addNewList(newList)
                }
            }
            newListDialog.show()
        }

        // Add spacing between items
//        val spacing = 48
//        binding.rvSavedLists.addItemDecoration(com.newsmead.custom.CustomDividerSpacer(spacing))

    }

    override fun onCardClick(listId: String, listName: String) {
        // Action
        val action = SavedFragmentDirections.actionSavedFragmentToSavedListArticlesFragment(
            null,
            listId,
            listName
        )

        Navigation.findNavController(requireView()).navigate(action)
    }
}