package com.newsmead

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.databinding.FragmentHomeBinding
import com.newsmead.recyclerviews.feed.FeedArticleAdapter
import com.newsmead.recyclerviews.feed.FeedHeaderAdapter
import com.newsmead.recyclerviews.feed.FeedHeaderViewHolder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var feedArticleAdapter: FeedArticleAdapter
    private lateinit var feedHeaderAdapter: FeedHeaderAdapter
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

        // Set up Feed RecyclerView
        data = DataHelper.loadArticleData()
        this.feedArticleAdapter = FeedArticleAdapter(data)
        this.feedHeaderAdapter = FeedHeaderAdapter()
        val concatAdapter = ConcatAdapter(feedHeaderAdapter, feedArticleAdapter)

        this.viewBinding.rvFeed.adapter = concatAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        this.viewBinding.rvFeed.layoutManager = linearLayoutManager

        // Add divider between items
        val customDividerItemDecoration = CustomDividerItemDecoration(context, R.drawable.line_divider, FeedHeaderViewHolder::class.java)
        this.viewBinding.rvFeed.addItemDecoration(customDividerItemDecoration)
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
}