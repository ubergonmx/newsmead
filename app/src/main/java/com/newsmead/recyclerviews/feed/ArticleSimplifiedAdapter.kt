package com.newsmead.recyclerviews.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemFeedArticleSimplifiedBinding
import com.newsmead.models.Article

class ArticleSimplifiedAdapter(
    private val articleList: ArrayList<Article>,
    private val clickListener: clickListener
): RecyclerView.Adapter<ArticleSimplifiedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleSimplifiedViewHolder {
        val itemViewBinding: ItemFeedArticleSimplifiedBinding = ItemFeedArticleSimplifiedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleSimplifiedViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ArticleSimplifiedViewHolder, position: Int) {
        holder.bindData(articleList[position])

        holder.getCardView().setOnClickListener {
            val article = articleList[position]
            clickListener.onItemClicked(
                article
            )
        }
    }

    fun updateData(articles: List<Article>) {
        articleList.clear()
        articleList.addAll(articles)
        notifyDataSetChanged()
    }

}