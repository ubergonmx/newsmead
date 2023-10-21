package com.newsmead.fragments.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.newsmead.ArticleActivity
import com.newsmead.DataHelper.loadRecommendedArticlesData

import com.newsmead.databinding.FragmentArticleBinding
import com.newsmead.databinding.ItemFeedArticleSimplifiedBinding
import com.newsmead.fragments.layouts.BottomSheetDialogSaveFragment

class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        // Supply linear layout with FeedArticleSimplified items (not a recycler view)
        val recommendedData = loadRecommendedArticlesData()
        for (article in recommendedData) {
            val itemFeedArticleSimplified = ItemFeedArticleSimplifiedBinding.inflate(inflater, container, false)
            itemFeedArticleSimplified.tvArticleTitle.text = article.title
            itemFeedArticleSimplified.tvSource.text = article.source
            itemFeedArticleSimplified.tvArticleDate.text = article.date
            itemFeedArticleSimplified.tvReadTime.text = article.readTime
            itemFeedArticleSimplified.ivSourceImage.setImageResource(article.imageId)

            // Add onClick to open article
            itemFeedArticleSimplified.root.setOnClickListener {
                val intent = Intent(requireContext(), ArticleActivity::class.java)
                intent.putExtra("articleId", "TEST")
                requireContext().startActivity(intent)
            }

            binding.llArticleRecommended.addView(itemFeedArticleSimplified.root)

        }

        // Bottom sheet dialog for saving articles
        binding.btnSaveList.setOnClickListener {
            val bottomSheetDialogFragment = BottomSheetDialogSaveFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "save")
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Add bottom app bar logic (it must stick and then only disappear
        // once it reaches recommendations
//        binding.btnArticleRecommendations.post {
//            val btnLocation = IntArray(2)
//            binding.btnArticleRecommendations.getLocationOnScreen(btnLocation)
//            val btnY = btnLocation[1]
//
//            val nestedScrollView = binding.nsvArticleText
//            val bottomAppBar = binding.bottomAppBar
//            nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
//                Log.d("Y_COORDINATE", "btnY: $btnY scrollY: $scrollY")
//                // Check if the scroll position is past the Y-coordinate of btnArticleRecommendations
//                if (scrollY > btnY -1000) {
//                    Log.d("SCROLL", "Hiding Bottom App Bar")
//                    bottomAppBar.performHide()
//                } else {
//                    bottomAppBar.performShow()
//                }
//            })
//        }


    }
}