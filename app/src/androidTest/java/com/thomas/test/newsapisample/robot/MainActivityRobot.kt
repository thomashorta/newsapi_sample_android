package com.thomas.test.newsapisample.robot

import androidx.test.core.app.ActivityScenario
import com.thomas.test.newsapisample.MainActivity
import com.thomas.test.newsapisample.R

class MainActivityRobot : BaseEspressoRobot() {
    lateinit var scenario: ActivityScenario<MainActivity>

    fun arrange(block: Arrange.() -> Unit) = Arrange().block()

    fun act(block: Act.() -> Unit) = Act().block()

    fun assert(block: Assert.() -> Unit) = Assert().block()

    fun onSourceList(block: SourceListFragmentRobot.() -> Unit) {
        block.invoke(SourceListFragmentRobot())
    }

    fun onArticleList(block: ArticleListFragmentRobot.() -> Unit) {
        block.invoke(ArticleListFragmentRobot())
    }

    fun onArticleContent(block: ArticleContentFragmentRobot.() -> Unit) {
        block.invoke(ArticleContentFragmentRobot())
    }

    inner class Arrange {
        fun launch() {
            scenario = ActivityScenario.launch(MainActivity::class.java)
        }
    }

    inner class Act {
        fun clickSourceListItem(position: Int) {
            clickRecyclerViewItem(R.id.rvSources, position)
        }
    }

    inner class Assert {
        fun sourceListIsVisible(): Unit = viewIsVisible(R.id.rvSources)

        fun sourceListIsNotEmpty(): Unit = recyclerViewIsNotEmpty(R.id.rvSources)
    }
}
