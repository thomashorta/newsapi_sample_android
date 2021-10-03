package com.thomas.test.newsapisample.feature.common

import android.content.Context
import androidx.fragment.app.Fragment
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.Source

open class BaseFragment : Fragment() {
    var activityCallback: ActivityCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActivityCallback) activityCallback = context
    }

    interface ActivityCallback {
        fun showSources()
        fun showArticles(source: Source)
        fun showContent(article: Article)
        fun updateTitle(title: String)
    }
}
