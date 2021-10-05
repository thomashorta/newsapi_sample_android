package com.thomas.test.newsapisample.util

import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.ArticleSource
import com.thomas.test.newsapisample.data.model.Source

object SourceFactory {
    fun createSource(suffix: String): Source = Source(
        category = "category $suffix",
        country = "country $suffix",
        description = "description $suffix",
        id = "id-$suffix",
        language = "lang",
        name = "name $suffix",
        url = "http://$suffix",
    )
}

object ArticleFactory {
    fun createArticle(suffix: String, sourceName: String = "source"): Article = Article(
        author = "author $suffix",
        content = "content $suffix",
        description = "description $suffix",
        publishedAt = "today",
        source = ArticleSource("", sourceName),
        title = "tile $suffix",
        url = "http://$suffix",
        urlToImage = null,
    )
}
