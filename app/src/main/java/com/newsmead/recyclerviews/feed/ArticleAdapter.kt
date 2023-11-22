package com.newsmead.recyclerviews.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.newsmead.R
import com.newsmead.databinding.FragmentArticleBinding
import com.newsmead.databinding.FragmentArticleSourceBinding
import com.newsmead.databinding.ItemFeedArticleBinding
import com.newsmead.fragments.article.ArticleSourceFragmentDirections
import com.newsmead.models.Article

class ArticleAdapter(
    private val articleList: ArrayList<Article>,
    private val clickListener: clickListener
): RecyclerView.Adapter<ArticleViewHolder>() {
    public var holders = ArrayList<ArticleViewHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val itemViewBinding: ItemFeedArticleBinding = ItemFeedArticleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ArticleViewHolder(itemViewBinding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bindData(articleList[position])

        // Add holder to holders
        holders.add(holder)

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

        // Add Navigation to ArticleFragment
//        holder.itemView.setOnClickListener { view ->
//            Log.d("ArticleAdapter", "Clicked on ${articleList[position].title}")
//            // Args
//            val bundle = bundleOf(
//                "articleId" to articleList[position].newsId,
//                "articleTitle" to articleList[position].title,
//                "articleSource" to articleList[position].source,
//                "articleDate" to articleList[position].date,
//                "articleImage" to articleList[position].sourceImage,
//            )
//
//            val navController = Navigation.findNavController(view)
//
//            navController.navigate(R.id.action_articleSourceFragment_to_articleFragment, bundle)
//        }
    }

}