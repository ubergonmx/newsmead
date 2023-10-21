package com.newsmead

import com.newsmead.models.Article
import java.text.DateFormat
import java.util.Date

import com.newsmead.models.SavedList
import kotlin.collections.ArrayList

// This is for testing purposes only. You can delete this class.
object DataHelper {
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
        data.add(SavedList("Read Later", 53))
        data.add(SavedList("Politics", 5))
        data.add(SavedList("Sports", 3))
        data.add(SavedList("manila updates", 2))
        data.add(SavedList("Business", 2))
        data.add(SavedList("Health Advice", 3))
        data.add(SavedList("Opinion", 1))
        data.add(SavedList("important news", 1))
        data.add(SavedList("NBA", 11))
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