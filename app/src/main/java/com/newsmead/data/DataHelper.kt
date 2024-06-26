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
            "abantenews", "Abante News" -> "source_abantenews"
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
            "abantenews" -> "Abante News"
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
            "Abante News" -> "abantenews"
            else -> ""
        }
    }

    fun translateArticle(id: String, useGoogle: Boolean, context: Context, callback: (String, String) -> Unit) {
        var url = "https://newsmead.southeastasia.cloudapp.azure.com/articles/translate/$id"
        if (useGoogle)
            url += "?service=google"
        val queue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val translatedTitle = response.getString("title")
                val translatedBody = response.getString("body")
                callback(translatedTitle, translatedBody)
            },
            { error ->
                Log.e("DataHelper", error.toString())
                callback("", "")
                return@JsonObjectRequest
            })
        queue.add(jsonObjectRequest)
    }

    fun loadArticleData(
        context: Context?,
        page: Int? = 1,
        source: String? = null,
        language: String? = null,
        category: String? = null,
        startDate: String? = null,
        endDate: String? = null,
        searchText : String? = null,
        pageSize: Int? = null,
        callback: (List<Article>) -> Unit)
    {
        // Create an empty ArrayList
        val articles = ArrayList<Article>()


        var baseUrl = "https://newsmead.southeastasia.cloudapp.azure.com"
        // Temporary fix for recommending Filipino articles - using the Filipino server
        if (language != null && language.lowercase() != "english") {
            baseUrl = "https://newsmead-fil.southeastasia.cloudapp.azure.com"
        }
        var articleUrl = "$baseUrl/articles/?page=$page"
        
        val uid = FirebaseHelper.getUid()
        var url = "$baseUrl/recommendations/$uid?page=$page"
        if (pageSize != null) {
            url += "&page_size=$pageSize"
            articleUrl += "&page_size=$pageSize"
        }
        if (category != null) {
            articleUrl += "&category=$category"
        }
        if (source != null) {
            articleUrl += "&source=$source"
        }
        if (searchText != null && searchText != "") {
            articleUrl += "&text=$searchText"
        }
        if (language != null) {
            url += "&language=$language"
            articleUrl += "&language=$language"
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
                            article.getString("category"),
                            article.getString("language"),
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
        // data.add("TV5 News")
        data.add("Abante News")
        return data
    }

    fun loadLanguagesData(): ArrayList<String> {
        val data = ArrayList<String>()
        data.add("Filipino")
        data.add("English")
        return data
    }

    fun getDateToday(): CharSequence {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date())
    }

}