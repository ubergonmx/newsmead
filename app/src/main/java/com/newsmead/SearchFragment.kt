package com.newsmead

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.newsmead.DataHelper.loadArticleDataLatest
import com.newsmead.DataHelper.loadCategoryData
import com.newsmead.DataHelper.loadSourcesData
import com.newsmead.databinding.ChipSearchBinding
import com.newsmead.databinding.FragmentSearchBinding
import com.newsmead.fragments.article.ArticleSourceFragment
import com.newsmead.recyclerviews.feed.FeedArticleSimplifiedAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val binding = FragmentSearchBinding.inflate(
            inflater, container, false
        )

        // Change text to date today m d, y
        binding.tvSearchDate.text = DataHelper.getDateToday()
        
        val categoryData = loadCategoryData()
        val sourcesData = loadSourcesData()

        // Clear existing chips
        binding.cgCategory.removeAllViews()
        binding.cgSources.removeAllViews()

        // Fill with chips
        for (category in categoryData) {
            val chip = ChipSearchBinding.inflate(inflater, null, false).root
            chip.text = category
            binding.cgCategory.addView(chip)
        }

        for (source in sourcesData) {
            val chip = ChipSearchBinding.inflate(inflater, null, false).root
            chip.text = source
            // Launch source fragment when chip is clicked
            chip.setOnClickListener(View.OnClickListener {
                Navigation.findNavController(it).navigate(com.newsmead.R.id.action_searchFragment_to_articleSourceFragment)
            })

            binding.cgSources.addView(chip)
        }

        val dataRecentNews = loadArticleDataLatest()
        binding.rvSearchLatestNews.adapter = FeedArticleSimplifiedAdapter(dataRecentNews)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSearchLatestNews.layoutManager = layoutManager

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}