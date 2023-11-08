package com.newsmead.fragments.article

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.newsmead.activities.ArticleActivity
import com.newsmead.data.DataHelper.loadRecommendedArticlesData
import com.newsmead.R

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

            // Add divider between items
            val viewDivider = View(requireContext())
            viewDivider.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1)
            viewDivider.setBackgroundResource(R.drawable.line_divider)
            binding.llArticleRecommended.addView(viewDivider)

        }

        // Set imageview
        binding.ivSourceImage.setImageResource(R.drawable.sample_source_image)


        // Bottom sheet dialog for saving articles
        binding.btnSaveList.setOnClickListener {
            val bottomSheetDialogFragment = BottomSheetDialogSaveFragment()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager, "save")
        }

        // Back button to go back to previous fragment
        binding.btnArticleBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Show more button to show more articles
        binding.btnArticleRecommendations.setOnClickListener {
            val articleSourceFragment = ArticleSourceFragment()
            val articleFragment = ArticleFragment()
            val args = Bundle()
            args.putString("sourceId", "INQUIRER.NET")
            articleFragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.flMainArticleContainer, articleSourceFragment)
                .addToBackStack(null)
                .commit()
        }

        // Share button to share article
        binding.btnArticleShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TITLE, "NewsMead")

                var articleText = binding.tvArticleHeadline.text.toString() + " by " + binding.tvSource.text.toString()
                putExtra(Intent.EXTRA_TEXT, articleText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, "Share this article")
            startActivity(shareIntent)
        }

        // Increase font size when btnArticleTextLarger is clicked
        binding.btnArticleTextLarger.setOnClickListener {
            binding.tvArticleText.textSize = binding.tvArticleText.textSize + 1
        }

        // Decrease font size when btnArticleTextSmaller is clicked
        binding.btnArticleTextSmaller.setOnClickListener {
            binding.tvArticleText.textSize = binding.tvArticleText.textSize - 1
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
            binding.nsvArticleText.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia))
            binding.bottomAppBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia))
            binding.clArticleTopBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia))
            binding.btnArticleTextLarger.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia_btn))
            binding.btnArticleTextSmaller.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia_btn))
            binding.btnSaveList.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.sepia_btn))
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