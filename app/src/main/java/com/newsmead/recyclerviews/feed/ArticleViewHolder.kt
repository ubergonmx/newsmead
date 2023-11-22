package com.newsmead.recyclerviews.feed

import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemFeedArticleBinding
import com.newsmead.fragments.article.ArticleSourceFragmentDirections
import com.newsmead.models.Article

class ArticleViewHolder(private val viewBinding: ItemFeedArticleBinding): RecyclerView.ViewHolder(viewBinding.root) {
    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(article: Article) {
        this.viewBinding.tvSource.text = article.source
        this.viewBinding.ivSourceImage.setImageResource(article.sourceImage)
        this.viewBinding.tvArticleTitle.text = article.title
        this.viewBinding.ivArticleImage.setImageResource(article.imageId)
        this.viewBinding.tvArticleDate.text = article.date
        this.viewBinding.tvReadTime.text = article.readTime

        this.viewBinding.root.setOnClickListener {v-> Navigation.findNavController(v)
            .navigate(ArticleSourceFragmentDirections.actionArticleSourceFragmentToArticleFragment(
                articleId = article.newsId,
                articleTitle = article.title,
                articleSource = article.source,
                articleImage = article.imageId.toString(),
                articleReadTime = article.readTime.toInt()
            ))

        }
    }
}