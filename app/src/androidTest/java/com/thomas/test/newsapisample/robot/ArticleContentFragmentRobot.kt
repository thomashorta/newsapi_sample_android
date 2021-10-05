package com.thomas.test.newsapisample.robot

import com.thomas.test.newsapisample.R

class ArticleContentFragmentRobot : BaseEspressoRobot() {
    fun arrange(block: Arrange.() -> Unit) = Arrange().block()

    fun act(block: Act.() -> Unit) = Act().block()

    fun assert(block: Assert.() -> Unit) = Assert().block()

    inner class Arrange

    inner class Act

    inner class Assert {
        fun contentTitleIsVisible(): Unit = viewIsVisible(R.id.tvContentTitle)
        fun contentAuthorIsVisible(): Unit = viewIsVisible(R.id.tvContentAuthor)
        fun contentTextIsVisible(): Unit = viewIsVisible(R.id.tvContentText)
        fun contentImageIsVisible(): Unit = viewIsVisible(R.id.ivContentImage)
    }
}
