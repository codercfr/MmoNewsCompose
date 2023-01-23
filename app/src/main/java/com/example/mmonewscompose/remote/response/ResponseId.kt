package com.example.mmonews.data.remote.response

data class ResponseId(
    val description: String,
    val developer: String,
    val game_url: String,
    val genre: String,
    var id: Int,
    val minimum_system_requirements: MinimumSystemRequirements,
    val platform: String,
    val profile_url: String,
    val publisher: String,
    val release_date: String,
    val screenshots: List<Screenshot>,
    val short_description: String,
    val status: String,
    val thumbnail: String,
    val title: String
)