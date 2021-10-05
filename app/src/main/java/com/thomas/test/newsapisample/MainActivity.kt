package com.thomas.test.newsapisample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.feature.articlecontent.ArticleContentFragment
import com.thomas.test.newsapisample.feature.articlelist.ArticleListFragment
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.feature.sourcelist.SourceListFragment

class MainActivity : AppCompatActivity(), BaseFragment.ActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showSources()
    }

    override fun showSources() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SourceListFragment.newInstance(), TAG_SOURCE_LIST)
            .commit()
    }

    override fun showArticles(source: Source) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("article")
            .replace(
                R.id.fragment_container,
                ArticleListFragment.newInstance(source),
                TAG_ARTICLE_LIST
            )
            .commit()
    }

    override fun showContent(article: Article) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("content")
            .replace(
                R.id.fragment_container,
                ArticleContentFragment.newInstance(article),
                TAG_ARTICLE_CONTENT
            )
            .commit()
    }

    override fun updateTitle(title: String) {
        supportActionBar?.title = title
    }

    companion object {
        const val TAG_SOURCE_LIST = "FRAG_SOURCES"
        const val TAG_ARTICLE_LIST = "FRAG_ARTICLES"
        const val TAG_ARTICLE_CONTENT = "FRAG_CONTENT"
    }
}
