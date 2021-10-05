package com.thomas.test.newsapisample.robot

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.feature.articlelist.ArticleListFragment

class ArticleListFragmentRobot : BaseEspressoRobot() {
    private lateinit var scenario: FragmentScenario<ArticleListFragment>

    fun arrange(block: Arrange.() -> Unit) = Arrange().block()

    fun act(block: Act.() -> Unit) = Act().block()

    fun assert(block: Assert.() -> Unit) = Assert().block()

    inner class Arrange {
        fun launch() {
            scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
        }
    }

    inner class Act {
        fun clickArticleListItem(position: Int) {
            clickRecyclerViewItem(R.id.rvArticles, position)
        }
    }

    inner class Assert {
        fun articleListIsVisible(): Unit = viewIsVisible(R.id.rvArticles)
        fun articleListIsNotEmpty(): Unit = recyclerViewIsNotEmpty(R.id.rvArticles)
    }
}
