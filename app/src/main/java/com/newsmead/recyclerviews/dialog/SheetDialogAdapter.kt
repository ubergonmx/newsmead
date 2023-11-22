package com.newsmead.recyclerviews.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemDialogListItemBinding
import com.newsmead.fragments.layouts.listListener
import com.newsmead.models.SavedList

class SheetDialogAdapter(
    private val listTitles: ArrayList<SavedList>,
    private val listener: listListener
) : RecyclerView.Adapter<SheetDialogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetDialogViewHolder {
        val itemViewBinding: ItemDialogListItemBinding = ItemDialogListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return SheetDialogViewHolder(itemViewBinding, listener)
    }

    override fun getItemCount(): Int {
        return listTitles.size
    }

    override fun onBindViewHolder(holder: SheetDialogViewHolder, position: Int) {
        holder.bindData(listTitles[position])
    }

    fun addNewList(newList: SavedList) {
        listTitles.add(newList)
        notifyDataSetChanged()
    }

}