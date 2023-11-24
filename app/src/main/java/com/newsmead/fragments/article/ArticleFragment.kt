package com.newsmead.fragments.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.newsmead.activities.ArticleActivity
import com.newsmead.data.DataHelper.loadRecommendedArticlesData
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.data.DataHelper
import com.newsmead.data.DatabaseHelper
import com.newsmead.data.FirebaseHelper

import com.newsmead.databinding.FragmentArticleBinding
import com.newsmead.databinding.ItemFeedArticleSimplifiedBinding
import com.newsmead.fragments.layouts.BottomSheetDialogSaveFragment
import com.newsmead.models.Article
import com.newsmead.models.SavedList
import com.newsmead.recyclerviews.feed.ArticleSimplifiedAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleFragment : Fragment(), clickListener {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var adapter: ArticleSimplifiedAdapter
    private lateinit var data: ArrayList<Article>
    private lateinit var savedLists: ArrayList<SavedList>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ArticleFragment", "onCreateView: ArticleFragment started")

        binding = FragmentArticleBinding.inflate(inflater, container, false)

        // Initialize RecyclerView
        data = ArrayList()
        adapter = ArticleSimplifiedAdapter(data, this)
        binding.rvArticleRecommended.adapter = adapter

        // Set layout manager
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvArticleRecommended.layoutManager = layoutManager

        // Add divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvArticleRecommended.addItemDecoration(customDivider)

        // Receive parcelable from ArticleActivity
        val article = arguments?.getParcelable<Article>("article") ?: Article(
            "Article Source",
            -1,
            "Article Title",
            0,
            "Article Date",
            "Article Content",
            "url",
            "0"
        )

        // Safeargs bundle
        Log.d("ArticleFragment", "onCreateView: newsId: ${article.newsId}")
        Log.d("ArticleFragment", "onCreateView: title: ${article.title}")
        Log.d("ArticleFragment", "onCreateView: date: ${article.date}")
        Log.d("ArticleFragment", "onCreateView: source: ${article.source}")
        Log.d("ArticleFragment", "onCreateView: sourceImage: ${article.sourceImage}")
        Log.d("ArticleFragment", "onCreateView: imageId: ${article.imageId}")
        Log.d("ArticleFragment", "onCreateView: readTime: ${article.readTime}")
        Log.d("ArticleFragment", "onCreateView: url: ${article.url}")

        // Check if start with "N" or "T"
        if (article.newsId.startsWith("N") || article.newsId.startsWith("T")) {
            // Add to history
            FirebaseHelper.addArticleToHistory(requireContext(), article)

            // Set article title
            binding.tvArticleHeadline.text = article.title

            // Set article source
            binding.tvSource.text = article.source

            // Set article image from link
            // parses articleImage link into image
            // Glide.with(this).load(article.articleImage).into(binding.ivSourceImage)
            binding.ivSourceImage.setImageResource(R.drawable.sample_source_image)

            // Set article read time
            val readTime = article.readTime + " min read"
            binding.tvArticleMinRead.text = readTime

            // Set article date (There's no date yet!)
            // binding.tvArticleDate.text = article.articleDate

        } else {
            Log.d("ArticleFragment", "onCreateView: Not adding to history (is a test article)")
            // Supply header values if able
            if (article.title != "Article Title") binding.tvArticleHeadline.text = article.title
            if (article.source != "Article Source") binding.tvSource.text = article.source

            // Supply everything else if able
            if (article.imageId != -1) {
                // parses articleImage link into image
                // Glide.with(this).load(article.articleImage).into(binding.ivSourceImage)
                binding.ivSourceImage.setImageResource(R.drawable.sample_source_image)
            }

//            if (article.articleBody != "Article Content") binding.tvArticleText.text = article.articleBody
            if (article.readTime != "0") {
                val readTime = article.readTime + " min read"
                binding.tvArticleMinRead.text = readTime
            }

            // if (article.articleDate != "Article Date") binding.tvArticleDate.text = article.articleDate
        }

        // Bottom sheet dialog for saving articles
        binding.btnSaveList.setOnClickListener {
            val bottomSheetDialogFragment = BottomSheetDialogSaveFragment(article)
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "save")
        }

        // Back button to go back to previous fragment
        binding.btnArticleBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Show more button to show more articles from source
        binding.btnArticleRecommendations.setOnClickListener {
            // Navigate to ArticleSourceFragment
            val action = ArticleFragmentDirections.actionArticleFragmentToArticleSourceFragment(
                article.source
            )

            Navigation.findNavController(binding.root).navigate(action)
        }

        // Share button to share article
        binding.btnArticleShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE, "NewsMead")

                val articleText = binding.tvArticleHeadline.text.toString() + " by " + binding.tvSource.text.toString()
                putExtra(Intent.EXTRA_TEXT, articleText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share this article")
            startActivity(shareIntent)
        }

        addBottomAppBarListeners()

        // Loading article content from url
        lifecycleScope.launch {
            // Check if offline article
            val articleId = article.newsId
            val offlineArticle = DatabaseHelper.getNewsArticleDao().getNewsArticle(articleId)

            Log.d("ArticleFragment", "onCreateView: offlineArticle: $offlineArticle")

            // Test load all offline articles
            val offlineArticles = DatabaseHelper.getNewsArticleDao().getAllNewsArticles()
            Log.d("ArticleFragment", "onCreateView: offlineArticles: $offlineArticles")

            if (offlineArticle != null && offlineArticle.articleBody.isNotEmpty()) {
                // Offline article
                binding.tvArticleText.text = offlineArticle.articleBody
            } else {
                // Online article
                // Insert API here
            }

            // Load Recommended Articles
            DataHelper.loadArticleData {
                if (FirebaseHelper.isNetworkAvailable(requireContext())) {
                    data.clear()
                    data.addAll(it)
                    adapter.notifyDataSetChanged()

                    // If no articles, hide recommendations
                    if (data.isEmpty()) {
                        binding.divider.visibility = View.GONE
                        binding.btnArticleRecommendations.visibility = View.GONE
                        binding.tvArticleRecommended.visibility = View.GONE
                    }

                } else {
                    binding.divider.visibility = View.GONE
                    binding.btnArticleRecommendations.visibility = View.GONE
                    binding.tvArticleRecommended.visibility = View.GONE
                }

            }

            // Load Saved Lists
            savedLists = ArrayList()

            val checkedLists = FirebaseHelper.checkIfArticleSavedInLists(requireContext(), article.newsId)
            for (listId in checkedLists) {
                val savedList = SavedList(listId, "List Name")
                savedLists.add(savedList)
            }

            Log.d("ArticleFragment", "onCreateView: savedLists: $savedLists")

            if (savedLists.isNotEmpty()) {
                Log.d("ArticleFragment", "onCreateView: savedLists is not empty")
                // Change save button icon to filled
                binding.btnSaveList.icon = ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_filled_weight400)
            }
        }

        return binding.root
    }

    fun convertPixelsToSp(px: Float): Float {
        return px / (requireContext().resources.displayMetrics.scaledDensity)
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

    private fun addBottomAppBarListeners() {
        // Increase font size when btnArticleTextLarger is clicked
        binding.btnArticleTextLarger.setOnClickListener {
            binding.tvArticleText.textSize = convertPixelsToSp(binding.tvArticleText.textSize + 1)
        }

        // Decrease font size when btnArticleTextSmaller is clicked
        binding.btnArticleTextSmaller.setOnClickListener {
            binding.tvArticleText.textSize = convertPixelsToSp(binding.tvArticleText.textSize) -3
        }

        // Set to light mode when btnArticleClrLight is clicked
        binding.btnArticleClrLight.setOnClickListener{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Set to dark mode when btnArticleClrDark is clicked
        binding.btnArticleClrDark.setOnClickListener{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        // Set to background color to F5EED9 when btnArticleClrSepia is clicked
        binding.btnArticleClrSepia.setOnClickListener{
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            val activity = requireActivity()
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = ContextCompat.getColor(activity,R.color.sepia);
            window.navigationBarColor = ContextCompat.getColor(activity,R.color.sepia);
            binding.root.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia))
            binding.nsvArticleText.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia))
            binding.bottomAppBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia))
            binding.clArticleTopBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia))
            binding.btnArticleTextLarger.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia_btn))
            binding.btnArticleTextSmaller.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia_btn))
            binding.btnSaveList.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia_btn))
        }
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = ArticleFragmentDirections.actionArticleFragmentSelf(article)
        Navigation.findNavController(binding.root).navigate(action)
    }
}