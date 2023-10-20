package com.newsmead.recyclerviews.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.Article
import com.newsmead.databinding.ItemFeedArticleBinding

class FeedArticleAdapter(private val articleList: ArrayList<Article>) : RecyclerView.Adapter<FeedArticleViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedArticleViewHolder {
        val itemViewBinding: ItemFeedArticleBinding = ItemFeedArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val feedArticleViewHolder = FeedArticleViewHolder(itemViewBinding)

        feedArticleViewHolder.itemView.setOnClickListener {
//            val intent : Intent = Intent(feedArticleViewHolder.itemView.context, ArticleDetailsActivity::class.java)
//
//            intent.putExtra(ArticleDetailsActivity.SOURCE_KEY, itemViewBinding.tvSource.text.toString())
//            intent.putExtra(ArticleDetailsActivity.TITLE_KEY, itemViewBinding.tvTitle.text.toString())
//            intent.putExtra(ArticleDetailsActivity.DATE_KEY, itemViewBinding.tvDate.text.toString())
//            intent.putExtra(ArticleDetailsActivity.READ_TIME_KEY, itemViewBinding.tvReadTime.text.toString())
//            intent.putExtra(ArticleDetailsActivity.URL_KEY, articleList[feedArticleViewHolder.adapterPosition].url)
//
//            feedArticleViewHolder.itemView.context.startActivity(intent)
        }
        return feedArticleViewHolder
    }

    override fun onBindViewHolder(holder: FeedArticleViewHolder, position: Int) {
        holder.bindData(articleList[position])
    }

    override fun getItemCount(): Int {
        return articleList.size
    }
}