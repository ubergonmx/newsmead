package com.newsmead.data

import android.icu.text.SimpleDateFormat
import android.util.Log
import com.newsmead.data.sources.RemoteDataSource
import com.newsmead.models.Article
import com.newsmead.models.NewsResponse
import java.text.DateFormat
import java.util.Date

import com.newsmead.models.SavedList
import com.newsmead.network.NewsAPIClientFactory
import java.util.Locale
import kotlin.collections.ArrayList

object DataHelper {

    fun formatTitle(title: String): String {
        // Remove the source from the title
        return title.substring(0, title.indexOf(" - "))
    }
    fun formatDate(date: String): String {
        // Format date "2023-10-31T13:03:00Z" string to Mmm dd, yyyy
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)

        val parsed = inputFormat.parse(date)
        return outputFormat.format(parsed)
    }
    fun loadArticleData(): ArrayList<Article> {
        val data = ArrayList<Article>()
        data.add(
            Article(
                "CNN",
                "Biden's first 100 days: What he's gotten done",
                "Apr 29, 2021",
                "9 min read",
                "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
            )
        )
        data.add(
            Article(
                "INQUIRER.NET",
                "BTS to perform new single 'Butter' at Billboard Music Awards",
                "Jan 28, 2022",
                "3 min read",
                "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
            )
        )
        data.add(
            Article(
                "ABS-CBN News",
                "Showbiz couple Kylie Padilla, Aljur Abrenica split",
                "Mar 12, 2023",
                "4 min read",
                "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
            )
        )
        data.add(
            Article(
                "Rappler",
                "Spacex launches 60 more Starlink satellites into orbit",
                "Dec 29, 2022",
                "5 min read",
                "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
            )
        )
        data.add(
            Article(
                "Manila Bulletin",
                "Duterte to meet with Chinese envoy over West Philippine Sea issue",
                "Nov 17, 2020",
                "7 min read",
                "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
            )
        )
        data.add(
            Article(
                "Philippine Star",
                "Comelec: 59 party-list groups to join 2022 polls",
                "Jul 02, 2022",
                "4 min read",
                "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
            )
        )
        return data
    }


    fun loadArticleData(callback: (List<Article>) -> Unit) {
        // Create an empty ArrayList
        val articles = ArrayList<Article>()

        // Create an instance of the DataRepository
        val dataRepository = DataRepository(RemoteDataSource(NewsAPIClientFactory.create()))

        // Fetch top headlines using the repository
        dataRepository.getTopHeadlines("ph", "13328281630540aaa6c2750b76b5ee12", object : DataRepository.DataCallback<NewsResponse> {
            override fun onSuccess(response: NewsResponse) {
                // Process the data here
                for (data in response.articles) {
                    Log.d("DataHelper", data.toString())
                    data.author?.let {
                        Article(
                            it,
                            formatTitle(data.title),
                            formatDate(data.publishedAt),
                            (3..9).random().toString() + " min read",
                            data.url
                        )
                    }?.let {
                        articles.add(
                            it
                        )
                    }
                }
                callback(articles)
            }

            override fun onError(error: Throwable) {
                // Handle errors
                Log.e("DataHelper", error.toString())
                articles.add(
                    Article(
                        "CNN",
                        "Biden's first 100 days: What he's gotten done",
                        "Apr 29, 2021",
                        "9 min read",
                        "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
                    )
                )
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
                        "ABS-CBN News",
                        "Showbiz couple Kylie Padilla, Aljur Abrenica split",
                        "Mar 12, 2023",
                        "4 min read",
                        "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
                    )
                )
                articles.add(
                    Article(
                        "Rappler",
                        "Spacex launches 60 more Starlink satellites into orbit",
                        "Dec 29, 2022",
                        "5 min read",
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
                        "Philippine Star",
                        "Comelec: 59 party-list groups to join 2022 polls",
                        "Jul 02, 2022",
                        "4 min read",
                        "https://www.cnn.com/2021/04/29/politics/biden-first-100-days/index.html"
                    )
                )
                callback(articles)
            }
        })
    }

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

    fun loadRecommendedArticlesData(): ArrayList<Article> {
        val data = ArrayList<Article>()
        data.add(
            Article(
                "CNN Philippines",
                "Recommended Article 1",
                "Oct 20, 2023",
                "9 min read",
                "http://www.cnnphilippines.com/news/2023/10/20/marcos-says-15k-filipinos-to-benefit-with-saudi-deal.html"
            )
        )
        data.add(
            Article(
                "INQUIRER.NET",
                "Recommended Article 2",
                "Oct 19, 2023",
                "9 min read",
                "http://www.cnnphilippines.com/news/2023/10/20/marcos-says-15k-filipinos-to-benefit-with-saudi-deal.html"
            )
        )
        data.add(
            Article(
                "ABS-CBN News",
                "Recommended Article 3",
                "Oct 18, 2023",
                "9 min read",
                "http://www.cnnphilippines.com/news/2023/10/20/marcos-says-15k-filipinos-to-benefit-with-saudi-deal.html"
            )
        )
        data.add(
            Article(
                "Rappler",
                "Recommended Article 4",
                "Oct 17, 2023",
                "9 min read",
                "http://www.cnnphilippines.com/news/2023/10/20/marcos-says-15k-filipinos-to-benefit-with-saudi-deal.html"
            )
        )
        return data
    }


    fun loadSearchArticlesData(): ArrayList<Article> {
        return loadSourceArticlesData()
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
        data.add("Health & Fitness")
        data.add("Travel")
        data.add("Money")
        return data
    }

    fun loadSourcesData(): ArrayList<String> {
        val data = ArrayList<String>()
        data.add("CNN Philippines")
        data.add("INQUIRER.NET")
        data.add("Rappler")
        data.add("The Manila Bulletin")
        data.add("ABS-CBN News")
        data.add("GMA Network")
        return data
    }

    fun loadListData(): ArrayList<SavedList> {
        val data = ArrayList<SavedList>()
        data.add(SavedList("A", "Read Later", loadArticleDataLatest()))
        data.add(SavedList("B", "Politics"))
        data.add(SavedList("C", "Sports"))
        data.add(SavedList("D", "manila updates"))
        data.add(SavedList("E", "Business"))
        data.add(SavedList("F", "Health Advice"))
        data.add(SavedList("G", "Opinion"))
        data.add(SavedList("H", "important news"))
        data.add(SavedList("I", "NBA"))
        return data
    }

    fun loadListNamesData(): ArrayList<String> {
        val data = ArrayList<String>()
        data.add("Read Later")
        data.add("Politics")
        data.add("Sports")
        data.add("manila updates")
        data.add("Business")
        data.add("Health Advice")
        data.add("Opinion")
        data.add("important news")
        data.add("NBA")
        return data
    }

    fun getDateToday(): CharSequence {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date())
    }

}