package com.newsmead.recyclerviews.saved

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemSavedListBinding
import com.newsmead.fragments.layouts.listListener
import com.newsmead.fragments.saved.cardListener
import com.newsmead.models.SavedList

class SavedListsAdapter(
    private val listSavedList: ArrayList<SavedList>,
    private val cardListener: cardListener
) : RecyclerView.Adapter<SavedListsViewHolder>(){

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
        return listSavedList.size
    }

    override fun onBindViewHolder(holder: SavedListsViewHolder, position: Int) {
        holder.bindData(listSavedList[position])

        // Add on click listener to card inside constraint layout
        holder.getCardView().setOnClickListener {
            cardListener.onCardClick(listSavedList[position].id)
        }
    }

    fun updateData(savedLists: List<SavedList>) {
        listSavedList.addAll(savedLists)
        notifyDataSetChanged()
    }

}