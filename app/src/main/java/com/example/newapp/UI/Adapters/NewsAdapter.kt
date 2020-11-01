package com.example.newapp.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newapp.Data.Model.Article
import com.example.newapp.R
import com.example.newapp.UI.Adapters.NewsAdapter.*
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<mArticleViewHolder>() {


    inner class mArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    private val differCallBack = object : DiffUtil.ItemCallback<Article>()
    {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mArticleViewHolder {
        return mArticleViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false))
    }

    override fun onBindViewHolder(holder: mArticleViewHolder, position: Int) {
        val current = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(current.urlToImage).into(ivArticleImage)
            tvTitle.text = current.title
            tvSource.text = current.source.name
            tvDescription.text = current.description
            tvPublishedAt.text = current.publishedAt
            setOnClickListener {
                onItemClickListener?.let {
                    it(current)
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

   private var onItemClickListener : ((Article) -> Unit)?=null
    fun setOnItemClickListener(listener : (Article)->Unit){
        onItemClickListener=listener
    }

}