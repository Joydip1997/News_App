package com.example.newapp.UI.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newapp.R
import com.example.newapp.Data.Repository.Resources
import com.example.newapp.InternetBroadCast
import com.example.newapp.UI.Activities.MainActivity
import com.example.newapp.UI.Adapters.NewsAdapter
import com.example.newapp.ViewModel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AllNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var paginationProgressBar : ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paginationProgressBar = view.findViewById(R.id.paginationProgressBar)
        newsViewModel = (activity as MainActivity).newsViewModel
        setUpRecyclerView()
        var job : Job? = null
        etSearch.addTextChangedListener {editable->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty())
                    {
                        newsViewModel.getSearchedNews(editable.toString())
                    }
                }
            }
        }
        newsViewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response)
            {
                is Resources.Success ->
                {
                    hideProgressbar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles)
                    }
                }
                is Resources.Error ->
                {
                    hideProgressbar()
                    response.message?.let {
                        Log.i("TAG",it)
                    }
                }
                is Resources.Loading ->
                {
                    showProgressbar()
                }

            }
        })
    }



    private fun setUpRecyclerView()
    {
        newsAdapter = NewsAdapter().apply {
            setOnItemClickListener {article->
                   val bundle = Bundle()
                   bundle.putSerializable("Article",article)
                   findNavController().navigate(R.id.action_allNewsFragment_to_articleFragment,bundle)
               }
        }
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }



    private fun hideProgressbar() {
        paginationProgressBar.visibility=View.GONE
    }

    private fun showProgressbar() {
        paginationProgressBar.visibility=View.VISIBLE
    }




}