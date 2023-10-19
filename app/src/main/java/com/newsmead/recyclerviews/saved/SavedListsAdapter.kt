package com.newsmead.recyclerviews.saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemSavedListBinding
class SavedListsAdapter(private val listList: ArrayList<List>) : RecyclerView.Adapter<SavedListsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedListsViewHolder {
        val itemViewBinding: ItemSavedListBinding = ItemSavedListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val savedListViewHolder = SavedListsViewHolder(itemViewBinding)

        savedListViewHolder.itemView.setOnClickListener {

        }

        return savedListViewHolder
    }

    override fun getItemCount(): Int {
        return listList.size
    }

    override fun onBindViewHolder(holder: SavedListsViewHolder, position: Int) {
        holder.bindData(listList[position])
    }

}