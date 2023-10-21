package com.newsmead.recyclerviews.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemDialogListItemBinding

class SheetDialogAdapter(private val listTitles: ArrayList<String>) : RecyclerView.Adapter<SheetDialogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetDialogViewHolder {
        val itemViewBinding: ItemDialogListItemBinding = ItemDialogListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val sheetDialogViewHolder = SheetDialogViewHolder(itemViewBinding)

        return sheetDialogViewHolder
    }

    override fun getItemCount(): Int {
        return listTitles.size
    }

    override fun onBindViewHolder(holder: SheetDialogViewHolder, position: Int) {
        holder.bindData(listTitles[position])
    }

}