package com.newsmead.recyclerviews.feed

import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemFeedArticleSimplifiedBinding
import com.newsmead.models.Article

class ArticleSimplifiedViewHolder(private val viewBinding: ItemFeedArticleSimplifiedBinding): RecyclerView.ViewHolder(viewBinding.root) {
    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(article: Article) {
        this.viewBinding.tvSource.text = article.source
        this.viewBinding.ivSourceImage.setImageResource(article.sourceImage)
        this.viewBinding.tvArticleTitle.text = article.title
//        this.viewBinding.tvArticleDate.text = article.date
        this.viewBinding.tvReadTime.text = article.readTime
    }

    fun getCardView() = viewBinding.cvArticleSimplifiedCard
}