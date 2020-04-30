package com.thomas.test.newsapisample.feature.articlecontent

import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.feature.articlelist.ArticleListViewModelTest
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.SpyK
import io.mockk.verifyOrder
import org.junit.After
import org.junit.Before

import org.junit.Test
import java.text.SimpleDateFormat

class ArticleContentViewModelTest {

    @SpyK
    lateinit var articleContentViewModel: ArticleContentViewModel

    @Before
    fun setUp() {
        articleContentViewModel = ArticleContentViewModel(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"))
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun `Given methods call WHEN loading an article THEN verify call order`() {
        val article = Article(
            "author",
            "content",
            "description",
            "2020-04-28T09:25:08Z",
            ArticleListViewModelTest.FAKE_SOURCE,
            "title1",
            "url",
            "urlToImage"
        )

        articleContentViewModel.loadContent(article)

        verifyOrder {
            articleContentViewModel.transformContent(any())
            articleContentViewModel.transformAuthor(any())
            articleContentViewModel.transformPublishTime(any())
        }
    }


    @After
    fun tearDown() {
    }
}