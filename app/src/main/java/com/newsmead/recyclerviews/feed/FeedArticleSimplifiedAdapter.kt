package com.newsmead.recyclerviews.feed

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.activities.ArticleActivity
import com.newsmead.models.Article
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

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ArticleActivity::class.java)
            intent.putExtra("articleId", "1")
            holder.itemView.context.startActivity(intent)
        }
    }

}