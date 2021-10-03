package com.thomas.test.newsapisample.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ArticleSource(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
) : Serializable
