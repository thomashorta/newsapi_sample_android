package com.thomas.test.newsapisample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.feature.sourcelist.SourceListFragment

class TestActivity : AppCompatActivity(), BaseFragment.ActivityCallback {
    var callback: BaseFragment.ActivityCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun startFragment(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun showSources() {
        callback?.showSources()
    }

    override fun showArticles(source: Source) {
        callback?.showArticles(source)
    }

    override fun showContent(article: Article) {
        callback?.showContent(article)
    }

    override fun updateTitle(title: String) {
        callback?.updateTitle(title)
    }
}
