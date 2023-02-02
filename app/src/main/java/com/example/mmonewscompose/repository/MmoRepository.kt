package com.example.mmonews.repository

import com.example.mmonews.data.remote.MmoApi
import com.example.mmonews.data.remote.response.ResponseApi
import com.example.mmonews.data.remote.response.ResponseApiItem
import com.example.mmonews.data.remote.response.ResponseId
import com.example.mmonews.util.Ressource
import com.example.mmonewscompose.remote.response.news.NewsApi
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class MmoRepository @Inject constructor(private val api:MmoApi) {
    suspend fun getMmoList(limit:Int, offset:Int):Ressource<ResponseApi>{
        val response = try {
            api.getMmoList(limit,offset)
        }catch ( e:java.lang.Exception){
            return Ressource.Error("An error occured")
        }
        return Ressource.Succes(response)
    }

    suspend fun getMmoName(id:Int):Ressource<ResponseId>{
        val response= try {
            api.getMmoInfo(id)
        }catch (e:java.lang.Exception){
            return Ressource.Error("An error occured")
        }
        return Ressource.Succes(response)
    }

    suspend fun getMmoNews(id:Int):Ressource<NewsApi>{
        val response= try {
            api.getMmoNews(id)
        }catch (e:java.lang.Exception){
            return Ressource.Error("An error occured")
        }
        return Ressource.Succes(response)
    }
}