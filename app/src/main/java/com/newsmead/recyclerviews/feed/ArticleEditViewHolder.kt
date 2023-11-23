package com.newsmead.recyclerviews.feed

import android.widget.CheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.databinding.ItemListEditBinding
import com.newsmead.models.Article

class ArticleEditViewHolder(
    private val binding: ItemListEditBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bindData(article: Article) {
        binding.tvArticleEditTitle.text = article.title
        binding.tvEditSource.text = article.source
        binding.tvArticleEditDate.text = article.date
        binding.tvEditReadTime.text = article.readTime
        binding.ivArticleEditImage.setImageResource(article.imageId)
        binding.ivSourceEditImage.setImageResource(article.sourceImage)
    }

    fun getCardView(): CardView {
        return binding.cvArticleEditCard
    }

    fun getCheckBox(): CheckBox {
        return binding.cbArticleEdit
    }

    fun toggleCheckBox() {
        binding.cbArticleEdit.isChecked = !binding.cbArticleEdit.isChecked
    }
}