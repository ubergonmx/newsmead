package com.newsmead.recyclerviews.feed

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemFeedArticleSimplifiedBinding
import com.newsmead.models.Article

class ArticleSimplifiedAdapter(
    private val articleList: ArrayList<Article>,
    private val clickListener: clickListener
): RecyclerView.Adapter<ArticleSimplifiedViewHolder>() {

    private var itemBackgroundColor : Int? = null
    private var itemTextColor : Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleSimplifiedViewHolder {
        if (itemBackgroundColor == null) {
            itemBackgroundColor = getSystemDefaultBackgroundColor(parent.context)
        }

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

        if (itemBackgroundColor != null) holder.getCardView().setBackgroundColor(itemBackgroundColor!!)
        if (itemTextColor != null) holder.setTextColors(itemTextColor!!)

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

    private fun getSystemDefaultBackgroundColor(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.windowBackground, typedValue, true)
        return typedValue.data
    }
    fun changeBackgroundColor(color: Int) {
        itemBackgroundColor = color
        notifyDataSetChanged()
    }

    fun changeTextColor(color: Int) {
        itemTextColor = color
        notifyDataSetChanged()
    }

}