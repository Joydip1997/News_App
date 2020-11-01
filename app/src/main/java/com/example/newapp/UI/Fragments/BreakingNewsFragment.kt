package com.example.newapp.UI.Fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newapp.R
import com.example.newapp.Data.Repository.Resources
import com.example.newapp.UI.Activities.MainActivity
import com.example.newapp.UI.Adapters.NewsAdapter
import com.example.newapp.ViewModel.NewsViewModel
import kotlinx.android.synthetic.main.fragment_breaking_news.*

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as MainActivity).newsViewModel
        setUpRecyclerView()

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

    private fun hideProgressbar() {
        paginationProgressBar.visibility=View.GONE
    }

    private fun showProgressbar() {
        paginationProgressBar.visibility=View.VISIBLE
    }


    private fun setUpRecyclerView()
    {
        newsAdapter = NewsAdapter().apply {
            setOnItemClickListener {article->
                val bundle = Bundle()
                bundle.putSerializable("Article",article)
                findNavController().navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
            }
        }
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }

    }
}