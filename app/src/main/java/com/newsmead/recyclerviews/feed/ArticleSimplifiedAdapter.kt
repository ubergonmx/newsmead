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

        holder.itemView.setOnClickListener {
            val parsedReadTime = articleList[position].readTime.split(" ")[0].toInt()
            clickListener.onItemClicked(
                articleId = articleList[position].newsId,
                articleTitle = articleList[position].title,
                articleSource = articleList[position].source,
                articleImage = articleList[position].imageId.toString(),
                articleReadTime = parsedReadTime
            )
        }
    }

    fun updateData(articles: List<Article>) {
        articleList.clear()
        articleList.addAll(articles)
        notifyDataSetChanged()
    }

}