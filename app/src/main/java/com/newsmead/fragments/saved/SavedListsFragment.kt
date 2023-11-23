package com.newsmead.fragments.saved
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.newsmead.data.DataHelper.loadListData
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.FragmentSavedYourListsBinding
import com.newsmead.fragments.layouts.NewListDialog
import com.newsmead.models.SavedList
import com.newsmead.recyclerviews.dialog.SheetDialogAdapter
import com.newsmead.recyclerviews.saved.SavedListsAdapter

class SavedListsFragment: Fragment() {
    private lateinit var binding: FragmentSavedYourListsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSavedYourListsBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get list of lists from firestore and move read later to top
        val data = ArrayList<SavedList>()

        if (auth.currentUser != null) {
            val userId = auth.currentUser?.uid.toString()
            firestore.collection("users").document(userId).collection("lists").get()
                .addOnSuccessListener { documents ->
                    var readLater: SavedList? = null
                    for (document in documents) {
                        val list = SavedList(document.data["id"].toString(), document.data["name"].toString())
                        if (list.title == "Read Later") {
                            readLater = list
                        }
                        data.add(list)
                    }

                    // Move the read later list at the top
                    if (readLater != null) {
                        data.remove(readLater)
                        data.add(0, readLater)
                    }

                    binding.rvSavedLists.adapter = SavedListsAdapter(data)
                    val layoutManager = LinearLayoutManager(activity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL

                    binding.rvSavedLists.layoutManager = layoutManager
                }
                .addOnFailureListener { exception ->
                    data += loadListData()

                    binding.rvSavedLists.adapter = SavedListsAdapter(data)
                    val layoutManager = LinearLayoutManager(activity)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL

                    binding.rvSavedLists.layoutManager = layoutManager
                }
        }

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
}