package com.thomas.test.newsapisample.feature.articlelist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.util.bind

class ArticleListAdapter(
    private val onItemClick: (Article) -> Unit
) : RecyclerView.Adapter<SourceListViewHolder>() {
    private val articles: MutableList<Article> = mutableListOf()

    fun setArticles(list: List<Article>) {
        articles.clear()
        articles.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = articles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceListViewHolder {
        return SourceListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SourceListViewHolder, position: Int) {
        holder.bind(articles[position], onItemClick)
    }
}

class SourceListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val ivArticleImage by view.bind<ImageView> { R.id.ivArticleImage }
    private val tvArticleTitle by view.bind<TextView> { R.id.tvArticleTitle }
    private val tvArticleAuthor by view.bind<TextView> { R.id.tvArticleAuthor }
    private val tvArticlePublication by view.bind<TextView> { R.id.tvArticlePublication }

    fun bind(article: Article, onItemClick: (Article) -> Unit) {
        with(itemView) {
            setOnClickListener {
                onItemClick(article)
            }

            ivArticleImage.load(article.urlToImage) {
                scale(Scale.FILL)
                crossfade(true)
                placeholder(R.color.colorAccent)
                error(R.color.colorAccent)
            }

            tvArticleTitle.text = article.title
            tvArticleAuthor.text = article.author
            tvArticlePublication.text = article.publishedAt
        }
    }

    companion object {
        fun create(parent: ViewGroup): SourceListViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_item, parent, false)
            return SourceListViewHolder(view)
        }
    }
}
