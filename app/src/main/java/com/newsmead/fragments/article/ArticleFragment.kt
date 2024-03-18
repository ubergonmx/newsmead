package com.newsmead.fragments.article

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
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
import java.util.Locale


class ArticleFragment() : Fragment(), clickListener, TextToSpeech.OnInitListener {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var adapter: ArticleSimplifiedAdapter
    private lateinit var savedLists: ArrayList<SavedList>
    private lateinit var textToSpeech: TextToSpeech
    private var isTranslated = false
    private enum class ColorMode { LIGHT, DARK, SEPIA }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ArticleFragment", "onCreateView: ArticleFragment started")
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        // Disable the Read Aloud button until the TextToSpeech engine is initialized
        binding.btnReadAloudArticle.isEnabled = false
        binding.btnReadAloudArticle.isClickable = false

        // Initialize TextToSpeech with Filipino language
        textToSpeech = TextToSpeech(requireContext(), this, "com.google.android.tts")

        // Initialize RecyclerView
        adapter = ArticleSimplifiedAdapter(arrayListOf(), this)
        binding.rvArticleRecommended.adapter = adapter

        // Set layout manager
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvArticleRecommended.layoutManager = layoutManager

        // Add divider
        val customDivider = CustomDividerItemDecoration(context, R.drawable.line_divider)
        binding.rvArticleRecommended.addItemDecoration(customDivider)

        // Receive parcelable from ArticleActivity
        var article = arguments?.getParcelable<Article>("article") ?: Article(
            "Article Source",
            "Article Source Image",
            "Article Title",
            "",
            "Article Date",
            "Article Body",
            "Article Content",
            "url",
            "0"
        )

        // If article is null from ArticleActivity, then receive from it's own arguments
        if (article.title == "Article Title") {
            val args = ArticleFragmentArgs.fromBundle(requireArguments())
            article = args.articleItem
        }

        // Safeargs bundle
        Log.d("ArticleFragment", "onCreateView: newsId: ${article.newsId}")
        Log.d("ArticleFragment", "onCreateView: title: ${article.title}")
        Log.d("ArticleFragment", "onCreateView: date: ${article.date}")
        Log.d("ArticleFragment", "onCreateView: source: ${article.source}")
        Log.d("ArticleFragment", "onCreateView: sourceImage: ${article.sourceImage}")
        Log.d("ArticleFragment", "onCreateView: imageURL: ${article.imageURL}")
        Log.d("ArticleFragment", "onCreateView: readTime: ${article.readTime}")
        Log.d("ArticleFragment", "onCreateView: url: ${article.url}")


        // Add to history
        FirebaseHelper.addArticleToHistory(requireContext(), article)

        // Set article title
        binding.tvArticleHeadline.text = article.title

        // Set article category
        binding.tvCategory.text = article.category

        // Set article source
        binding.tvSource.text = article.source
        binding.btnArticleRecommendations.text = "Show more from " + article.source
        val context = binding.root.context
        val resourceId = context.resources.getIdentifier(article.sourceImage, "drawable", context.packageName)
        binding.ivSourceImage.setImageResource(if (resourceId != 0) resourceId else R.drawable.sample_source_image)

        // Set article read time
        val readTime = article.readTime //+ " min read"
        binding.tvArticleMinRead.text = readTime

        // Read Aloud button
        binding.btnReadAloudArticle.setOnClickListener {
            binding.btnReadAloudArticle.isEnabled = false
            binding.btnReadAloudArticle.isClickable = false
            if (textToSpeech.isSpeaking && binding.btnReadAloudArticle.text == "Stop") {
                binding.btnReadAloudArticle.text = "Read Aloud"
                textToSpeech.stop()
            } else {
                var body = binding.tvArticleText.text.toString()
                Log.d("ArticleFragment", "tts-body: ${body.substring(0, 100)}")
                if (body.isNotEmpty()) speak(body)
                else Toast.makeText(context, "No article to read", Toast.LENGTH_SHORT).show()
            }
            binding.btnReadAloudArticle.isEnabled = true
            binding.btnReadAloudArticle.isClickable = true
        }

        // Translate button
        binding.btnTranslateArticle.setOnClickListener {
            if (textToSpeech.isSpeaking) {
                binding.btnReadAloudArticle.isEnabled = false
                binding.btnReadAloudArticle.isClickable = false
                binding.btnReadAloudArticle.text = "Read Aloud"
                textToSpeech.stop()
                binding.btnReadAloudArticle.isEnabled = true
                binding.btnReadAloudArticle.isClickable = true
            }

            binding.btnTranslateArticle.isEnabled = false
            binding.btnTranslateArticle.isClickable = false
            binding.btnTranslateArticle.text = "Translating..."
            if (!isTranslated) {
                // Translate article
                DataHelper.translateArticle(article.newsId, binding.switchUseGoogle.isChecked, requireContext()) { title, body ->
                    if(title.isNotEmpty() && body.isNotEmpty()) {
                        binding.tvArticleHeadline.text = title
                        binding.tvArticleText.text = body
                        binding.btnTranslateArticle.text = "English"
                        isTranslated = true
                    }
                    else{
                        Toast.makeText(context, "Translation unavailable", Toast.LENGTH_SHORT).show()
                        binding.btnTranslateArticle.text = "Filipino"
                    }
                    binding.btnTranslateArticle.isEnabled = true
                    binding.btnTranslateArticle.isClickable = true
                }
            } else {
                // Revert to original language
                binding.tvArticleHeadline.text = article.title
                binding.tvArticleText.text = article.body
                binding.btnTranslateArticle.text = "Filipino"
                isTranslated = false
                binding.btnTranslateArticle.isEnabled = true
                binding.btnTranslateArticle.isClickable = true
            }
        }

        // When the user zooms in on the ZoomImageView, disable the NestedScrollView scrolling
        binding.ivArticleFullImage.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    // Enable scrolling on the NestedScrollView when the user lifts their finger
                    binding.nsvArticleText.setScrollingEnabled(true)
                }
                else -> {
                    // Disable scrolling on the NestedScrollView for any other MotionEvent
                    binding.nsvArticleText.setScrollingEnabled(false)
                }
            }
            false // Return false so the event is not consumed and continues to be propagated
        }

        // Bottom sheet dialog for saving articles
        binding.btnSaveList.setOnClickListener {
            binding.btnSaveList.isEnabled = false
            binding.btnSaveList.isClickable = false
            val bodyContent = binding.tvArticleText.text.toString()
            val bottomSheetDialogFragment = BottomSheetDialogSaveFragment(article, bodyContent)
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "save")
            // Re-enable the button after a 1 second to prevent multiple rapid clicks
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnSaveList.isEnabled = true
                binding.btnSaveList.isClickable = true
            }, 1000)
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
            binding.btnArticleShare.isEnabled = false
            binding.btnArticleShare.isClickable = false
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

            // Re-enable the button after a 2 seconds to prevent multiple rapid clicks
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnArticleShare.isEnabled = true
                binding.btnArticleShare.isClickable = true
            }, 2000)
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
                    Glide.with(this@ArticleFragment).load(article.imageURL).error(R.drawable.sample_article_image).into(binding.ivArticleFullImage)
                } else {
                    binding.ivSourceImage.setImageResource(R.drawable.sample_source_image)
                }
            }

            // Load Recommended Articles
            DataHelper.loadArticleData(context, pageSize = 10) { it ->
                // Check if the fragment is still attached to a context
                val context = context ?: return@loadArticleData

                if (FirebaseHelper.isNetworkAvailable(context)) {
                    // remove article with the same newsId
                    val recommendedArticles = it.filter { it.newsId != article.newsId }
                    adapter.updateData(recommendedArticles)

                    // If no articles, hide recommendations
                    if (it.isEmpty()) {
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
    // Implement TextToSpeech.OnInitListener
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Set language to Filipino (Tagalog)
            val result = textToSpeech.setLanguage(Locale("fil", "PH"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                installVoiceData()
            }
            for (voice in textToSpeech.voices) {
                // Log the voice name
                Log.d("ArticleFragment", "${voice.name} ${voice.quality} ${voice.isNetworkConnectionRequired} ${voice.features} ${voice.latency}")
                if(voice.name.contains("fil-ph-x-fie-local")) {
                    textToSpeech.setVoice(voice)
                    break
                }
            }

            textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(p0: String?) {
                    // Change the Read Aloud button to "Stop" when TTS starts speaking
                    Handler(Looper.getMainLooper()).post {
                        binding.btnReadAloudArticle.text = "Stop"
                        Log.d("ArticleFragment", "TTS started")
                    }
                }

                override fun onDone(utteranceId: String) {
                    // Enable the Read Aloud button after the TTS is done speaking
                    Handler(Looper.getMainLooper()).post {
                        binding.btnReadAloudArticle.text = "Read Aloud"
                        binding.btnReadAloudArticle.isEnabled = true
                        binding.btnReadAloudArticle.isClickable = true
                        Log.d("ArticleFragment", "TTS done")
                    }
                }

                override fun onError(p0: String?){
                }

                override fun onError(utteranceId: String?, errorCode: Int) {
                    // Show toast message when TTS encounters an error
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Read Aloud failed (${errorCode})", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            binding.btnReadAloudArticle.isEnabled = true
            binding.btnReadAloudArticle.isClickable = true
        } else {
            // Upon TextToSpeech initialization failure, disable the Read Aloud button
            Toast.makeText(context, "Read Aloud is not available", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Ask the current default engine to launch the matching INSTALL_TTS_DATA activity
     * so the required TTS files are properly installed.
     */
    private fun installVoiceData() {
        val intent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.google.android.tts")
        try {
            Log.v("TTS", "Installing voice data: " + intent.toUri(0))
            startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Log.e("TTS", "Failed to install TTS data, no activity found for $intent)")
        }
    }

    // Don't forget to release TextToSpeech when your activity is destroyed
    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }

    // Function to convert text to speech
    private fun speak(text: String) {
        if(!isTranslated){
            textToSpeech.setLanguage(Locale.ENGLISH)
            textToSpeech.voice = textToSpeech.voices.find { it.name.contains("en-us-x-iom-local") }
        }
        else{
            textToSpeech.setLanguage(Locale("fil", "PH"))
            textToSpeech.voice = textToSpeech.voices.find { it.name.contains("fil-ph-x-fie-local") }
        }
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, TextToSpeech.ACTION_TTS_QUEUE_PROCESSING_COMPLETED)
    }

    fun convertPixelsToSp(px: Float): Float {
        return px / (requireContext().resources.displayMetrics.scaledDensity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Add bottom app bar logic (it must stick and then only disappear
        // once it reaches recommendations
        // binding.btnArticleRecommendations.post {
        //     val btnLocation = IntArray(2)
        //     binding.btnArticleRecommendations.getLocationOnScreen(btnLocation)
        //     val btnY = btnLocation[1]
        //     val nestedScrollView = binding.nsvArticleText
        //     val bottomAppBar = binding.bottomAppBar
        //     nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
        //         Log.d("Y_COORDINATE", "btnY: $btnY scrollY: $scrollY")
        //         // Check if the scroll position is past the Y-coordinate of btnArticleRecommendations
        //         if (scrollY > btnY -1000) {
        //             Log.d("SCROLL", "Hiding Bottom App Bar")
        //             bottomAppBar.performHide()
        //         } else {
        //             bottomAppBar.performShow()
        //         }
        //     })
        // }
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
        // Change the color of the Recommended Articles RecyclerView (background)
        adapter.changeBackgroundColor(color)
    }
    
    private fun changeColorOfButtons(color: Int) {
        binding.btnArticleTextSmaller.setBackgroundColor(color)
        binding.btnSaveList.setBackgroundColor(color)
        binding.btnTranslateArticle.setBackgroundColor(color)
        binding.btnReadAloudArticle.setBackgroundColor(color)
    }

    private fun changeColorOfSubTexts(color: Int){
        binding.tvArticleAuthor.setTextColor(color)
        binding.tvArticleMinRead.setTextColor(color)
        binding.tvArticleBy.setTextColor(color)
        binding.tvByDot.setTextColor(color)
        binding.tvBullet.setTextColor(color)
        // Change the color of the Recommended Articles RecyclerView (read time)
        adapter.changeSubTextColor(color)
    }

    private fun changeColorOfTexts(color: Int){
        binding.tvArticleText.setTextColor(color)
        binding.tvArticleHeadline.setTextColor(color)
        binding.tvArticleRecommended.setTextColor(color)
        binding.tvSource.setTextColor(color)
        binding.tvCategory.setTextColor(color)

        binding.btnArticleTextLarger.setTextColor(color)
        binding.btnArticleTextSmaller.setTextColor(color)
        binding.btnSaveList.setTextColor(color)
        binding.btnTranslateArticle.setTextColor(color)
        binding.btnReadAloudArticle.setTextColor(color)
        binding.btnArticleClrLight.setTextColor(color)
        binding.btnArticleClrDark.setTextColor(color)
        binding.btnArticleClrSepia.setTextColor(color)

        // Change icon colors
        binding.btnArticleBack.setColorFilter(color)
        binding.btnArticleShare.setColorFilter(color)

        binding.btnSaveList.iconTint = ColorStateList.valueOf(color)
        binding.btnTranslateArticle.iconTint = ColorStateList.valueOf(color)
        binding.btnReadAloudArticle.iconTint = ColorStateList.valueOf(color)

        binding.btnArticleTextLarger.setCompoundDrawableTintList(ColorStateList.valueOf(color))
        binding.btnArticleTextSmaller.setCompoundDrawableTintList(ColorStateList.valueOf(color))

        // Change the color of the Recommended Articles RecyclerView (source, title)
        adapter.changeTextColor(color)
    }

    override fun onItemClicked(article: Article) {
        // Action
        val action = ArticleFragmentDirections.actionArticleFragmentSelf(article)
        Navigation.findNavController(requireView()).navigate(action)
    }
}