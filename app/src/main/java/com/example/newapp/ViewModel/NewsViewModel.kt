package com.example.newapp.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newapp.Data.Model.Article
import com.example.newapp.Data.Model.LocationModel
import com.example.newapp.Data.Model.NewsResponse
import com.example.newapp.Data.Repository.Repository
import com.example.newapp.Data.Repository.Resources
import com.example.newapp.InternetBroadCast
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(
    val repository: Repository
) : ViewModel() {

    val breakingNews : MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    val searchedNews : MutableLiveData<Resources<NewsResponse>> = MutableLiveData()
    val breakingNewsPage = 1



//    init {
//        getBreakingNews("in")
//    }



    fun getBreakingNews(countryCode : String) = viewModelScope.launch {
        breakingNews.postValue(Resources.Loading())
        val response = repository.getBreakingNews(countryCode)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun getSearchedNews(searchQuery : String) = viewModelScope.launch {
        searchedNews.postValue(Resources.Loading())
        val response = repository.getAllNews(searchQuery)
        breakingNews.postValue(handleSearchedNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse>
    {
        if(response.isSuccessful)
        {
            response.body()?.let {
                return Resources.Success(it)
            }
        }

            return Resources.Error(response.message())

    }

    private fun handleSearchedNewsResponse(response: Response<NewsResponse>) : Resources<NewsResponse>
    {
        if(response.isSuccessful)
        {
            response.body()?.let {
                return Resources.Success(it)
            }
        }

        return Resources.Error(response.message())

    }




    fun upsertArtcile(article: Article) = viewModelScope.launch {
        repository.upsertArticles(article)
    }

    fun deleteArtcile(article: Article) = viewModelScope.launch {
        repository.deleteArticles(article)
    }

    fun getAllSavedArticles() = repository.getAllSavedArticles()


}