package com.example.mmonewscompose.remote.response.news

data class NewsApiItem(
    val article_content: String,
    val article_url: String,
    val id: Int,
    val main_image: String,
    val short_description: String,
    val thumbnail: String,
    val title: String
)