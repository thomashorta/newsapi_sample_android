package com.thomas.test.newsapisample.robot

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import com.thomas.test.newsapisample.R
import com.thomas.test.newsapisample.feature.articlecontent.ArticleContentFragment

class ArticleContentFragmentRobot : BaseEspressoRobot() {
    private lateinit var scenario: FragmentScenario<ArticleContentFragment>

    fun arrange(block: Arrange.() -> Unit) = Arrange().block()

    fun act(block: Act.() -> Unit) = Act().block()

    fun assert(block: Assert.() -> Unit) = Assert().block()

    inner class Arrange {
        fun launch() {
            scenario = launchFragmentInContainer(themeResId = R.style.AppTheme)
        }
    }

    inner class Act

    inner class Assert {
        fun contentTitleIsVisible(): Unit = viewIsVisible(R.id.tvContentTitle)
        fun contentAuthorIsVisible(): Unit = viewIsVisible(R.id.tvContentAuthor)
        fun contentTextIsVisible(): Unit = viewIsVisible(R.id.tvContentText)
        fun contentImageIsVisible(): Unit = viewIsVisible(R.id.ivContentImage)
    }
}
