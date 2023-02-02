package com.example.mmonews.data.remote

import com.example.mmonews.data.remote.response.ResponseApi
import com.example.mmonews.data.remote.response.ResponseApiItem
import com.example.mmonews.data.remote.response.ResponseId
import com.example.mmonews.data.remote.response.ResponseIdArray
import com.example.mmonewscompose.remote.response.news.NewsApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MmoApi {

    @GET("games")
    suspend fun getMmoList(
        @Query("limit")limit:Int,
        @Query("offset")offset:Int,
    ):ResponseApi

    @GET("game")
    suspend fun getMmoInfo(
        @Query("id") id:Int
    ):ResponseId

    @GET("latestnews")
    suspend fun getMmoNews(
        @Query("id") id:Int
    ):NewsApi
}