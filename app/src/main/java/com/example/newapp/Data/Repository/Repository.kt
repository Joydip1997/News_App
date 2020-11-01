package com.example.newapp.Data.Repository

import com.example.newapp.Data.API.RetrofitInstance
import com.example.newapp.Data.DB.ArticleRoomDatabase
import com.example.newapp.Data.Model.Article


class Repository(val articleRoomDatabase: ArticleRoomDatabase) {

    suspend fun getBreakingNews(countryCode : String) = RetrofitInstance.api.getAllBreakingNews(countryCode)
    suspend fun getAllNews(searchQuery : String) = RetrofitInstance.api.getSearchedNews(searchQuery)
    suspend fun upsertArticles(article: Article) = articleRoomDatabase.getArticleDao().upsert(article)
    fun getAllSavedArticles() = articleRoomDatabase.getArticleDao().getAllArticles()
    suspend fun deleteArticles(article: Article) = articleRoomDatabase.getArticleDao().deleteArticles(article)

}