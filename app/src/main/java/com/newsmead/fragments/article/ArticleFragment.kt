package com.newsmead.fragments.article

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.bumptech.glide.Glide
import com.newsmead.R
import com.newsmead.custom.CustomDividerItemDecoration
import com.newsmead.data.DataHelper
import com.newsmead.data.DatabaseHelper
import com.newsmead.data.FirebaseHelper

import com.newsmead.databinding.FragmentArticleBinding
import com.newsmead.fragments.layouts.BottomSheetDialogSaveFragment
import com.newsmead.models.Article
import com.newsmead.models.SavedList
import com.newsmead.recyclerviews.feed.ArticleSimplifiedAdapter
import com.newsmead.recyclerviews.feed.clickListener
import kotlinx.coroutines.launch

class ArticleFragment : Fragment(), clickListener {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var adapter: ArticleSimplifiedAdapter
    private lateinit var data: ArrayList<Article>
    private lateinit var savedLists: ArrayList<SavedList>
    private enum class ColorMode { LIGHT, DARK, SEPIA }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            "",
            "Article Date",
            "Article Body",
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
        Log.d("ArticleFragment", "onCreateView: imageURL: ${article.imageURL}")
        Log.d("ArticleFragment", "onCreateView: readTime: ${article.readTime}")
        Log.d("ArticleFragment", "onCreateView: url: ${article.url}")


        // Add to history
        FirebaseHelper.addArticleToHistory(requireContext(), article)

        // Set article title
        binding.tvArticleHeadline.text = article.title

        // Set article source
        binding.tvSource.text = article.source
        binding.btnArticleRecommendations.text = "Show more from " + article.source
        binding.ivSourceImage.setImageResource(article.sourceImage)

        // Set article read time
        val readTime = article.readTime //+ " min read"
        binding.tvArticleMinRead.text = readTime


        // Bottom sheet dialog for saving articles
        binding.btnSaveList.setOnClickListener {
            val bodyContent = binding.tvArticleText.text.toString()
            val bottomSheetDialogFragment = BottomSheetDialogSaveFragment(article, bodyContent)
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

                val articleTitle = article.title
                val articleURL = article.url
                val shareText = "$articleTitle\n$articleURL"

                putExtra(Intent.EXTRA_TEXT, shareText)
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

            if (offlineArticle != null && offlineArticle.articleBody.isNotEmpty()) {
                // Offline article
                binding.tvArticleText.text = offlineArticle.articleBody
            } else {
                // Online article
                binding.tvArticleText.text = article.body

                // Set article image from link
                if (article.imageURL != "") {
                    Glide.with(this@ArticleFragment).load(article.imageURL).into(binding.ivArticleFullImage)
                } else {
                    binding.ivSourceImage.setImageResource(R.drawable.sample_source_image)
                }
            }

            // Load Recommended Articles
            DataHelper.loadArticleData(context) {
                // Check if the fragment is still attached to a context
                val context = context ?: return@loadArticleData

                if (FirebaseHelper.isNetworkAvailable(context)) {
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

                // Change text to "Saved to list"
                val savedText = "Saved to list"
                binding.btnSaveList.text = savedText
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
        val size = 2
        val minSizeLimit = 14
        val maxSizeLimit = 60

        // Increase font size when btnArticleTextLarger is clicked
        binding.btnArticleTextLarger.setOnClickListener {
            val newSize = convertPixelsToSp(binding.tvArticleText.textSize + size)
            if (newSize <= maxSizeLimit) {
                binding.tvArticleText.textSize = newSize
            }
            else{
                Toast.makeText(context, "Reached maximum text size", Toast.LENGTH_SHORT).show()
            }
        }

        // Decrease font size when btnArticleTextSmaller is clicked
        binding.btnArticleTextSmaller.setOnClickListener {
            val newSize = convertPixelsToSp(binding.tvArticleText.textSize) - size
            if (newSize > minSizeLimit) {
                binding.tvArticleText.textSize = newSize
            }
            else{
                Toast.makeText(context, "Reached minimum text size", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnArticleClrLight.setOnClickListener{ updateColors(ColorMode.LIGHT) }
        binding.btnArticleClrDark.setOnClickListener{ updateColors(ColorMode.DARK) }
        binding.btnArticleClrSepia.setOnClickListener{ updateColors(ColorMode.SEPIA) }
    }

    /**
     * Updates the colors of the article fragment
     * @param color The color mode to update to
     */
    private fun updateColors(color: ColorMode) {
        val activity = requireActivity()
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        when (color) {
            ColorMode.LIGHT -> {
                window.statusBarColor = ContextCompat.getColor(activity,R.color.light)
                window.navigationBarColor = ContextCompat.getColor(activity,R.color.light)

                changeColorOfBackgrounds(ContextCompat.getColor(requireContext(), R.color.light))
                changeColorOfButtons(ContextCompat.getColor(requireContext(), R.color.light_btn))
                binding.btnArticleTextLarger.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_btn_emphasis))
                changeColorOfTexts(ContextCompat.getColor(requireContext(), R.color.light_text))
                changeColorOfSubTexts(ContextCompat.getColor(requireContext(), R.color.light_subtext))
            }
            ColorMode.DARK -> {
                window.statusBarColor = ContextCompat.getColor(activity,R.color.dark)
                window.navigationBarColor = ContextCompat.getColor(activity,R.color.dark)

                changeColorOfBackgrounds(ContextCompat.getColor(requireContext(), R.color.dark))
                changeColorOfButtons(ContextCompat.getColor(requireContext(), R.color.dark_btn))
                binding.btnArticleTextLarger.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.dark_btn_emphasis))
                changeColorOfTexts(ContextCompat.getColor(requireContext(), R.color.dark_text))
                changeColorOfSubTexts(ContextCompat.getColor(requireContext(), R.color.dark_subtext))
            }
            ColorMode.SEPIA -> {
                window.statusBarColor = ContextCompat.getColor(activity,R.color.sepia)
                window.navigationBarColor = ContextCompat.getColor(activity,R.color.sepia)

                changeColorOfBackgrounds(ContextCompat.getColor(requireContext(), R.color.sepia))
                changeColorOfButtons(ContextCompat.getColor(requireContext(), R.color.sepia_btn))
                binding.btnArticleTextLarger.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia_btn_emphasis))
                changeColorOfTexts(ContextCompat.getColor(requireContext(), R.color.sepia_text))
                changeColorOfSubTexts(ContextCompat.getColor(requireContext(), R.color.sepia_subtext))
            }
        }
    }

    private fun changeColorOfBackgrounds(color: Int){
        binding.root.setBackgroundColor(color)
        binding.nsvArticleText.setBackgroundColor(color)
        binding.bottomAppBar.setBackgroundColor(color)
        binding.clArticleTopBar.setBackgroundColor(color)
    }
    
    private fun changeColorOfButtons(color: Int) {
        binding.btnArticleTextSmaller.setBackgroundColor(color)
        binding.btnSaveList.setBackgroundColor(color)
    }

    private fun changeColorOfSubTexts(color: Int){
        binding.tvArticleAuthor.setTextColor(color)
        binding.tvByDot.setTextColor(color)
        binding.tvArticleMinRead.setTextColor(color)
        binding.tvArticleBy.setTextColor(color)
    }

    private fun changeColorOfTexts(color: Int){
        binding.tvArticleText.setTextColor(color)
        binding.tvArticleHeadline.setTextColor(color)
        binding.tvArticleRecommended.setTextColor(color)
        binding.tvSource.setTextColor(color)

        binding.btnArticleTextLarger.setTextColor(color)
        binding.btnArticleTextSmaller.setTextColor(color)
        binding.btnSaveList.setTextColor(color)
        binding.btnArticleClrLight.setTextColor(color)
        binding.btnArticleClrDark.setTextColor(color)
        binding.btnArticleClrSepia.setTextColor(color)

        // Change icon colors
        binding.btnArticleBack.setColorFilter(color)
        binding.btnArticleShare.setColorFilter(color)

        binding.btnSaveList.iconTint = ColorStateList.valueOf(color)

        binding.btnArticleTextLarger.setCompoundDrawableTintList(ColorStateList.valueOf(color))
        binding.btnArticleTextSmaller.setCompoundDrawableTintList(ColorStateList.valueOf(color))
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = ArticleFragmentDirections.actionArticleFragmentSelf(article)
        Navigation.findNavController(binding.root).navigate(action)
    }
}