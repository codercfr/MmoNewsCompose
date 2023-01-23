package com.example.mmonews.di

import com.example.mmonews.data.remote.MmoApi
import com.example.mmonews.data.remote.response.ResponseApi
import com.example.mmonews.repository.MmoRepository
import com.example.mmonews.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMmoRepository(
        api:MmoApi
    ):MmoRepository{
        return MmoRepository(api)
    }

    @Singleton
    @Provides
    fun providePokeApi(): MmoApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(MmoApi::class.java)
    }

}