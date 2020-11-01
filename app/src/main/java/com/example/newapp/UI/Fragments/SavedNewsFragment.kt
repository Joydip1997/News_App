package com.example.newapp.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.newapp.R
import com.example.newapp.UI.Activities.MainActivity
import com.example.newapp.UI.Adapters.NewsAdapter
import com.example.newapp.ViewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved_news.*
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.android.synthetic.main.fragment_search_news.rvSearchNews
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private val isListEmpty : MutableLiveData<Boolean> = MutableLiveData()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as MainActivity).newsViewModel
        setUpRecyclerView()
        newsViewModel.getAllSavedArticles().observe(viewLifecycleOwner, Observer {article->
           MainScope().launch {
               delay(1000L)
               article?.let {
                   newsAdapter.differ.submitList(article)
               }
           }
        })

        val itemSwipeListener = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        )
        {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               val pos = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[pos]
                newsViewModel.deleteArtcile(article)
                Snackbar.make(view,"Article Deleted",Snackbar.LENGTH_LONG).apply {
                    show()
                    setAction("Undo")
                    {
                        newsViewModel.upsertArtcile(article)
                    }
                }
            }

        }

       ItemTouchHelper(itemSwipeListener).apply {
           attachToRecyclerView(rvSavedNews)
       }


        isListEmpty()

        isListEmpty.observe(viewLifecycleOwner, Observer {
            if(it)
            {
                svMessage.visibility=View.VISIBLE
                rvSavedNews.visibility-View.GONE
            }
            else
            {
                svMessage.visibility=View.GONE
                rvSavedNews.visibility-View.VISIBLE
            }
        })

    }

    private fun isListEmpty()
    {
        newsViewModel.getAllSavedArticles().observe(viewLifecycleOwner, Observer {list->
            if(list.isEmpty())
            {
                isListEmpty.postValue(true)
            }
            else
            {
                isListEmpty.postValue(false)
            }
        })
    }

    private fun setUpRecyclerView()
    {
        newsAdapter = NewsAdapter().apply {
            setOnItemClickListener {article->
                val bundle = Bundle()
                bundle.putSerializable("Article",article)
                findNavController().navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle)
            }
        }
        rvSavedNews.apply {
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