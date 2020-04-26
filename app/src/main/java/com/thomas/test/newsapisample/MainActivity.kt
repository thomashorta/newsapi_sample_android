package com.thomas.test.newsapisample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.feature.articlelist.ArticleListFragment
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.feature.articlecontent.ArticleContentFragment
import com.thomas.test.newsapisample.feature.sourcelist.SourceListFragment

class MainActivity : AppCompatActivity(), BaseFragment.ActivityCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showSources()
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is BaseFragment) fragment.activityCallback = this
    }

    override fun showSources() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, SourceListFragment.newInstance())
            .commit()
    }

    override fun showArticles(source: Source) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("article")
            .replace(R.id.fragment_container, ArticleListFragment.newInstance(source))
            .commit()
    }

    override fun showContent(article: Article) {
        supportFragmentManager.beginTransaction()
            .addToBackStack("content")
            .replace(R.id.fragment_container, ArticleContentFragment.newInstance(article))
            .commit()
    }

    override fun updateTitle(title: String) {
        supportActionBar?.title = title
    }

}
