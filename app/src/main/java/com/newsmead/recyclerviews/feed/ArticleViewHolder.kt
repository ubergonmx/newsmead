package com.newsmead.recyclerviews.feed

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.newsmead.R
import com.newsmead.databinding.ItemFeedArticleBinding
import com.newsmead.models.Article

class ArticleViewHolder(private val viewBinding: ItemFeedArticleBinding): RecyclerView.ViewHolder(viewBinding.root) {
    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(article: Article) {
        this.viewBinding.tvSource.text = article.source
        val context = viewBinding.root.context
        val resourceId = context.resources.getIdentifier(article.sourceImage, "drawable", context.packageName)
        this.viewBinding.ivSourceImage.setImageResource(if (resourceId != 0) resourceId else R.drawable.sample_source_image)
        this.viewBinding.tvArticleTitle.text = article.title
        if (article.imageURL != null && article.imageURL != "")
            Glide.with(this.viewBinding.root).load(article.imageURL).into(this.viewBinding.ivArticleImage)
        else
            this.viewBinding.ivArticleImage.setImageResource(article.imageId ?: R.drawable.sample_article_image)
        this.viewBinding.tvArticleDate.text = article.date
        this.viewBinding.tvReadTime.text = article.readTime
    }

    fun getCardView() = viewBinding.cvArticleCard
}