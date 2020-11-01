package com.example.newapp.Data.API

import com.example.newapp.Constants.NEWS_API_KEY
import com.example.newapp.Data.Model.LocationModel
import com.example.newapp.Data.Model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getAllBreakingNews(
        @Query("country")
        countryCode : String="us",
        @Query("page")
        page : Int = 1,
        @Query("apikey")
        apiKey : String = NEWS_API_KEY
    ) : Response<NewsResponse>


    @GET("v2/top-headlines")
    suspend fun getSearchedNews(
        @Query("q")
        searchQuery : String,
        @Query("page")
        page : Int = 1,
        @Query("apikey")
        apiKey : String = NEWS_API_KEY
    ) : Response<NewsResponse>






}