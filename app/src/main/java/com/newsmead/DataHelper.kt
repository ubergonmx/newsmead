package com.newsmead

import java.text.DateFormat
import java.time.LocalDate
import java.util.ArrayList
import java.util.Date

import com.newsmead.recyclerviews.saved.List

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

    fun loadListData(): ArrayList<List> {
        val data = ArrayList<List>()
        data.add(List("Politics", 5))
        data.add(List("Sports", 3))
        data.add(List("manila updates", 2))
        data.add(List("Business", 2))
        data.add(List("Health Advice", 3))
        data.add(List("Opinion", 1))
        data.add(List("important news", 1))
        data.add(List("NBA", 11))
        return data
    }

    fun getDateToday(): CharSequence {
        return DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date())
    }
}