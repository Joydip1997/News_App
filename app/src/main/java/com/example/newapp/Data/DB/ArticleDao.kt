package com.example.newapp.Data.DB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newapp.Data.Model.Article


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article) : Long

    @Query("SELECT * FROM `Article Table`")
    fun getAllArticles() : LiveData<List<Article>>

    @Delete
    suspend fun deleteArticles(article: Article)
}