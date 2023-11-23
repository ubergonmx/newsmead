package com.newsmead.recyclerviews.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemListEditBinding
import com.newsmead.fragments.layouts.listListener
import com.newsmead.models.Article

class ArticleEditAdapter(
    private val articleList: ArrayList<Article>,
    private val clickListener: listListener
): RecyclerView.Adapter<ArticleEditViewHolder>() {
    private val checkBoxes: ArrayList<CheckBox> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleEditViewHolder {
        val itemViewBinding: ItemListEditBinding = ItemListEditBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ArticleEditViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ArticleEditViewHolder, position: Int) {
        holder.bindData(articleList[position])

        // Add checkbox to list
        checkBoxes.add(holder.getCheckBox())

        holder.getCardView().setOnClickListener {
            clickListener.onListChecked(
                listId  = articleList[position].newsId
            )

            // Toggle checkbox
            holder.toggleCheckBox()
        }
    }

    fun updateData(articles: List<Article>) {
        articleList.clear()
        articleList.addAll(articles)
        notifyDataSetChanged()
    }

    fun uncheckAll() {
        for (checkBox in checkBoxes) {
            checkBox.isChecked = false
        }
    }

    fun checkAll() {
        for (checkBox in checkBoxes) {
            checkBox.isChecked = true
        }
    }
}