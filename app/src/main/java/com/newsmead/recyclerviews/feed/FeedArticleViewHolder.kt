package com.newsmead.recyclerviews.feed

import androidx.recyclerview.widget.RecyclerView
import com.newsmead.models.Article
import com.newsmead.databinding.ItemFeedArticleBinding

class FeedArticleViewHolder(private val viewBinding: ItemFeedArticleBinding): RecyclerView.ViewHolder(viewBinding.root) {

    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(article: Article) {
        this.viewBinding.tvSource.text = article.source
        this.viewBinding.ivSourceImage.setImageResource(article.sourceImage)
        this.viewBinding.tvArticleTitle.text = article.title
        this.viewBinding.ivArticleImage.setImageResource(article.imageId)
        this.viewBinding.tvArticleDate.text = article.date
        this.viewBinding.tvReadTime.text = article.readTime
    }

}