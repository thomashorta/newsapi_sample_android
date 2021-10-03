package com.thomas.test.newsapisample.data.model

data class ArticlePO(
    val title: String,
    val description: String,
    val content: String,
    val author: String,
    val url: String,
    val imageUrl: String?,
    val timeSincePublish: String?
)
