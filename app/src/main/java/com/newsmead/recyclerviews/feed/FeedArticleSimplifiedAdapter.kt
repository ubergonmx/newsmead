package com.newsmead.recyclerviews.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.Article
import com.newsmead.databinding.ItemFeedArticleSimplifiedBinding

class FeedArticleSimplifiedAdapter(private val articleList: ArrayList<Article>) : RecyclerView.Adapter<FeedArticleSimplifiedViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FeedArticleSimplifiedViewHolder {
        val itemViewBinding: ItemFeedArticleSimplifiedBinding = ItemFeedArticleSimplifiedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val feedArticleSimplifiedViewHolder = FeedArticleSimplifiedViewHolder(itemViewBinding)

        feedArticleSimplifiedViewHolder.itemView.setOnClickListener {

        }

        return feedArticleSimplifiedViewHolder
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: FeedArticleSimplifiedViewHolder, position: Int) {
        holder.bindData(articleList[position])
    }

}