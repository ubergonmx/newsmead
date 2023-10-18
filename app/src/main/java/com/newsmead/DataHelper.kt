package com.newsmead

import java.util.ArrayList

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
                " 29, 2022",
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
}