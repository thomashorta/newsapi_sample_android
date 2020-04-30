package com.thomas.test.newsapisample.feature.articlecontent

import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.feature.articlelist.ArticleListViewModelTest
import com.thomas.test.newsapisample.feature.common.extension.containsAny
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.SpyK
import io.mockk.mockkStatic
import io.mockk.verifyOrder
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
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
        mockkStatic("com.thomas.test.newsapisample.feature.common.extension.StringExtKt")
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

    @Test
    fun `Given a author name WHEN is VALID THEN verify characters`() {
        val author = "Joseph Climber"
        assertFalse(author.containsAny(ArticleContentViewModel.INVALID_AUTHOR_CHARACTERS))
    }

    @Test
    fun `Given a author name WHEN is INVALID THEN verify characters`() {
        val author = "[Joseph] [Climber]"
        assertTrue(author.containsAny(ArticleContentViewModel.INVALID_AUTHOR_CHARACTERS))
    }

    @After
    fun tearDown() {
    }
}