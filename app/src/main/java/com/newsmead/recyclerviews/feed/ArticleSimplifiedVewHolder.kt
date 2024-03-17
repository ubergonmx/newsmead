package com.newsmead.recyclerviews.feed

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.R
import com.newsmead.databinding.ItemFeedArticleSimplifiedBinding
import com.newsmead.models.Article

class ArticleSimplifiedViewHolder(private val viewBinding: ItemFeedArticleSimplifiedBinding): RecyclerView.ViewHolder(viewBinding.root) {
    // Performs the binding of the article data to the views in the ViewHolder
    fun bindData(article: Article) {
        this.viewBinding.tvSource.text = article.source
        val context = viewBinding.root.context
        val resourceId = context.resources.getIdentifier(article.sourceImage, "drawable", context.packageName)
        this.viewBinding.ivSourceImage.setImageResource(if (resourceId != 0) resourceId else R.drawable.sample_source_image)
        this.viewBinding.tvArticleTitle.text = article.title
        this.viewBinding.tvReadTime.text = article.readTime
    }

    fun getCardView() = viewBinding.cvArticleSimplifiedCard

    fun setBackgroundColor(itemBackgroundColor: Int) {
        this.viewBinding.clArticleSimplified.setBackgroundColor(itemBackgroundColor)
    }

    fun setTextColors(itemTextColor: Int) {
        this.viewBinding.tvSource.setTextColor(itemTextColor)
        this.viewBinding.tvArticleTitle.setTextColor(itemTextColor)
    }

    fun setSubTextColor(itemTextColor: Int) {
        this.viewBinding.tvReadTime.setTextColor(itemTextColor)
    }

}