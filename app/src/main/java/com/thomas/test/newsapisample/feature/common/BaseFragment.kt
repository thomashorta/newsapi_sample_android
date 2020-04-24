package com.thomas.test.newsapisample.feature.common

import androidx.fragment.app.Fragment
import com.thomas.test.newsapisample.data.model.Source

open class BaseFragment : Fragment() {
    var activityCallback: ActivityCallback? = null

    interface ActivityCallback {
        fun showSources()
        fun showArticles(source: Source)
        fun updateTitle(title: String)
    }
}