package com.newsmead.fragments.bottomnavigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.data.DataHelper
import com.newsmead.data.FirebaseHelper
import com.newsmead.databinding.FragmentHomeBinding
import com.newsmead.models.Article
import com.newsmead.recyclerviews.feed.ArticleAdapter
import com.newsmead.recyclerviews.feed.clickListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), clickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var shimmerFrameLayout : ShimmerFrameLayout
    private lateinit var viewBinding: FragmentHomeBinding

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
        this.viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return this.viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up Shimmer
        this.shimmerFrameLayout = viewBinding.shimmerFeed
        this.shimmerFrameLayout.startShimmer()

        // Set up Feed RecyclerView
        //data = DataHelper.loadArticleData()
        this.articleAdapter = ArticleAdapter(data, this)
        // Load data and update the adapter when available
        DataHelper.loadArticleData { articles ->
            // Stop the shimmer
            this.shimmerFrameLayout.stopShimmer()
            this.shimmerFrameLayout.visibility = View.GONE

            if (FirebaseHelper.isNetworkAvailable(requireContext())) {
                // Update the adapter with the retrieved articles
                articleAdapter.updateData(articles)
            } else {
                this.shimmerFrameLayout.visibility = View.VISIBLE
            }

        }

        this.viewBinding.rvFeed.adapter = articleAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        this.viewBinding.rvFeed.layoutManager = linearLayoutManager

        // Divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider)
        this.viewBinding.rvFeed.addItemDecoration(customDivider)

        // History Button
        this.viewBinding.btnHistory.setOnClickListener {
            // Action
            val action = HomeFragmentDirections.actionHomeFragmentToHistoryFragment()

            // Navigate
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    companion object {
        private var data = ArrayList<Article>()
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = HomeFragmentDirections.actionHomeFragmentToArticleActivityStart(
            article
        )

        // Navigate
        Navigation.findNavController(requireView()).navigate(action)
    }
}