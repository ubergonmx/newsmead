package com.newsmead.recyclerviews.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.Article
import com.newsmead.databinding.ItemFeedArticleSimplifiedBinding

class SearchLatestArticleAdapter(private val articleList: ArrayList<Article>) : RecyclerView.Adapter<SearchLatestArticleViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchLatestArticleViewHolder {
        val itemViewBinding: ItemFeedArticleSimplifiedBinding = ItemFeedArticleSimplifiedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val searchLatestArticleViewHolder = SearchLatestArticleViewHolder(itemViewBinding)

        searchLatestArticleViewHolder.itemView.setOnClickListener {

        }

        return searchLatestArticleViewHolder
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: SearchLatestArticleViewHolder, position: Int) {
        holder.bindData(articleList[position])
    }

}