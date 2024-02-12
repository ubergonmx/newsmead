package com.newsmead.data

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.newsmead.R
import com.newsmead.models.Article
import java.text.DateFormat
import java.util.Date

import java.util.Locale
import kotlin.collections.ArrayList

object DataHelper {

    fun formatTitle(title: String): String {
        // Remove the source from the title
        return title.substring(0, title.indexOf(" - "))
    }
    fun formatDate(date: String): String {
        // Format date "2023-10-31T13:03:00Z" string to Mmm dd, yyyy
        // val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)

        // Format date "2023-10-31 13:03:00" string to Mmm dd, yyyy
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

        val parsed = inputFormat.parse(date)
        return outputFormat.format(parsed)
    }

    // Source image mapping
    fun sourceImageMap(source: String): String {
        return when (source) {
            "gmanews", "GMA News" -> "source_gmanews"
            "inquirer", "INQUIRER.NET" -> "source_inquirer"
            "philstar", "Philstar" -> "source_philstar"
            "manilabulletin", "The Manila Bulletin" -> "source_manilabulletin"
            "news5", "TV5 News" -> "source_news5"
            else -> "sample_source_image"
        }
    }

    // Source name mapping
    fun sourceNameMap(source: String): String {
        return when (source) {
            "gmanews" -> "GMA News"
            "inquirer" -> "INQUIRER.NET"
            "philstar" -> "Philstar"
            "manilabulletin" -> "The Manila Bulletin"
            "news5" -> "TV5 News"
            else -> ""
        }
    }

    // Reverse source name mapping
    fun reverseSourceNameMap(fullName: String): String {
        return when (fullName) {
            "GMA News" -> "gmanews"
            "INQUIRER.NET" -> "inquirer"
            "Philstar" -> "philstar"
            "The Manila Bulletin" -> "manilabulletin"
            "TV5 News" -> "news5"
            else -> ""
        }
    }


    fun loadArticleData(
        context: Context?,
        page: Int? = 1,
        source: String? = null,
        category: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        searchText : String? = null,
        callback: (List<Article>) -> Unit)
    {
        // Create an empty ArrayList
        val articles = ArrayList<Article>()

        val baseUrl = "https://newsmead.southeastasia.cloudapp.azure.com"
        var articleUrl = "$baseUrl/articles/?page=$page"
        
        val uid = FirebaseHelper.getUid()
        var url = "$baseUrl/recommendations/$uid/$page"

        if (category != null) {
            articleUrl += "&category=$category"
        }
        if (source != null) {
            articleUrl += "&source=$source"
        }
        if (searchText != null) {
            articleUrl += "&text=$searchText"
        }
        if (startDate != null) {
            articleUrl += "&startDate=$startDate"
        }
        if (endDate != null) {
            articleUrl += "&endDate=$endDate"
        }

        if (searchText != null || source != null || category != null) {
            url = articleUrl
        }
        Log.d("DataHelper", "URL: $url")
        // Fetch articles using Volley
        val queue = Volley.newRequestQueue(context)

        // Request a JsonObject response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                // Parse the JSON response.
                val data = response.getJSONArray("articles")
                // Use the body string here.
                Log.d("DataHelper", "Articles: ${data.length()}")
                // Process the data here
                for (i in 0 until data.length()) {
                    val article = data.getJSONObject(i)
                    Log.d("DataHelper", article.toString())
                    articles.add(
                        Article(
                            sourceNameMap(article.getString("source")),
                            sourceImageMap(article.getString("source")),
                            article.getString("title"),
                            article.getString("image_url"),
                            formatDate(article.getString("date")),
                            article.getString("body"),
                            article.getString("read_time"),
                            article.getString("url"),
                            article.getInt("article_id").toString()
                        )
                    )
                }
                callback(articles)
            },
            { error ->
                Log.e("DataHelper", error.toString())
                articles.add(
                    Article(
                        "INQUIRER.NET",
                        "BTS to perform new single 'Butter' at Billboard Music Awards",
                        "Jan 28, 2022",
                        "3 min read",
                        "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
                    )
                )
                articles.add(
                    Article(
                        "Manila Bulletin",
                        "Duterte to meet with Chinese envoy over West Philippine Sea issue",
                        "Nov 17, 2020",
                        "7 min read",
                        "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
                    )
                )
                articles.add(
                    Article(
                        "Philstar",
                        "Comelec: 59 party-list groups to join 2022 polls",
                        "Jul 02, 2022",
                        "4 min read",
                        "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
                    )
                )
                callback(articles)
            })

        queue.add(jsonObjectRequest)
    }



    // ----------------------------------------------------------------------------- //
    // ----------------------------- Data for Testing ------------------------------ //
    // ----------------------------------------------------------------------------- //

    fun loadArticleDataLatest(): ArrayList<Article> {
        val data = ArrayList<Article>()
        data.add(
            Article(
                "CNN Philippines",
                "PH secures over \$4.26-B investment deals from Marcos' Saudi Arabia visit",
                "Oct 20, 2023",
                "9 min read",
                "http://www.cnnphilippines.com/news/2023/10/20/marcos-says-15k-filipinos-to-benefit-with-saudi-deal.html"
            )
        )
        data.add(
            Article(
                "Philstar.com",
                "CHED to extend educational assistance to children of slain Filipinos in Israel",
                "Oct 20, 2023",
                "5 min read",
                "https://www.philstar.com/headlines/2023/10/20/2305252/ched-extend-educational-assistance-children-slain-filipinos-israel"
            )
        )
        data.add(
            Article(
                "ABS-CBN News",
                "Napoles found guilty, lawmaker acquitted in P20-M PDAF case",
                "Oct 20, 2023",
                "4 min read",
                "https://news.abs-cbn.com/news/10/20/23/napoles-found-guilty-lawmaker-acquitted-in-p20-m-pdaf-case"
            )
        )
        return data
    }

    fun loadCategoryArticlesData(): ArrayList<Article> {
        return loadSourceArticlesData()
    }

    fun loadSourceArticlesData(): ArrayList<Article> {
        val data = ArrayList<Article>()
        data.add(
            Article(
                "INQUIRER.NET",
                "Extraordinary spaces",
                "Oct 21, 2023",
                "9 min read",
                "https://business.inquirer.net/426355/extraordinary-spaces"
            )
        )
        data.add(
            Article(
                "INQUIRER.NET",
                "So uncharacteristic of a buyer",
                "Oct 21, 2023",
                "5 min read",
                "https://business.inquirer.net/426359/so-uncharacteristic-of-a-buyer"
            )
        )
        data.add(
            Article(
                "INQUIRER.NET",
                "When provinces take charge",
                "Oct 21, 2023",
                "7 min read",
                "https://opinion.inquirer.net/167207/when-provinces-take-charge"
            )
        )
        data.add(
            Article(
                "INQUIRER.NET",
                "Inquirer’s ‘Rebound,’ anniversary issues bag Marketing Excellence Awards",
                "Oct 15, 2023",
                "5 min read",
                "https://newsinfo.inquirer.net/1845771/inquirers-rebound-anniversary-issues-bag-marketing-excellence-awards"
            )
        )
        data.add(
            Article(
                "INQUIRER.NET",
                "Japan's MUFG still bullish on PH growth",
                "Oct 21, 2023",
                "4 min read",
                "https://business.inquirer.net/427512/japans-mufg-still-bullish-on-ph-growth"
            )
        )

        return data
    }

    fun loadCategoryData(): ArrayList<String> {
        val data = ArrayList<String>()
        data.add("News")
        data.add("Opinion")
        data.add("Sports")
        data.add("Technology")
        data.add("Lifestyle")
        data.add("Business")
        data.add("Entertainment")
        return data
    }

    fun loadCategoryLongerData(): ArrayList<String> {
        val data = ArrayList<String>()
        data.add("News")
        data.add("Opinion")
        data.add("Sports")
        data.add("Technology")
        data.add("Lifestyle")
        data.add("Business")
        data.add("Entertainment")
        return data
    }

    fun loadSourcesData(): ArrayList<String> {
        val data = ArrayList<String>()
        data.add("GMA News")
        data.add("INQUIRER.NET")
        data.add("Philstar")
        data.add("The Manila Bulletin")
        data.add("TV5 News")
        return data
    }

    fun getDateToday(): CharSequence {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date())
    }

}