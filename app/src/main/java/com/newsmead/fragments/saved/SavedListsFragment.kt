package com.newsmead.fragments.saved
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.DataHelper.loadListData
import com.newsmead.databinding.FragmentSavedYourListsBinding
import com.newsmead.recyclerviews.saved.SavedListsAdapter

class SavedListsFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSavedYourListsBinding.inflate(inflater, container, false)

        val data = loadListData()

        binding.rvSavedLists.adapter = SavedListsAdapter(data)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rvSavedLists.layoutManager = layoutManager

        return binding.root
    }
}