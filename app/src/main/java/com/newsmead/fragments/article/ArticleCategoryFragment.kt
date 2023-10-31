package com.newsmead.fragments.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.data.DataHelper
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.databinding.ChipSearchBinding
import com.newsmead.databinding.FragmentArticleCategoryBinding
import com.newsmead.recyclerviews.feed.FeedArticleAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ArticleCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ArticleCategoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentArticleCategoryBinding.inflate(
            inflater, container, false
        )

        //RecyclerView of Articles
        val categoryArticlesData = DataHelper.loadCategoryArticlesData()
        binding.rvCategoryArticles.adapter = FeedArticleAdapter(categoryArticlesData)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvCategoryArticles.layoutManager = layoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvCategoryArticles.addItemDecoration(customDividerItemDecoration)

        // Fill chips
        val chipData = DataHelper.loadSourcesData()
        binding.cgSources.removeAllViews()
        for (category in chipData) {
            val chip = ChipSearchBinding.inflate(inflater, container, false).root
            chip.text = category
            binding.cgSources.addView(chip)
        }

        // Back button to go back to previous fragment
        binding.btnArticleRecommendedBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArticleCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArticleCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}