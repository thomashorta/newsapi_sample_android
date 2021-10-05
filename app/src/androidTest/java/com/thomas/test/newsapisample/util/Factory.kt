package com.thomas.test.newsapisample.util

import com.thomas.test.newsapisample.data.model.Source

object SourceFactory {
    fun createSource(suffix: String): Source = Source(
        category = "category $suffix",
        country = "country $suffix",
        description = "description $suffix",
        id = "id-$suffix",
        language = "lang",
        name = "name $suffix",
        url = "http://$suffix"
    )
}
