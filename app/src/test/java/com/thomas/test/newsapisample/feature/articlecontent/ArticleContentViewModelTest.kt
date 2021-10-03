package com.thomas.test.newsapisample.feature.articlecontent

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.ArticleSource
import com.thomas.test.newsapisample.feature.common.extension.isValidAuthor
import com.thomas.test.newsapisample.getOrAwaitValue
import io.mockk.*
import io.mockk.impl.annotations.SpyK
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat

class ArticleContentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @SpyK
    lateinit var articleContentViewModel: ArticleContentViewModel

    @Before
    fun setUp() {
        articleContentViewModel =
            ArticleContentViewModel(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"))
        MockKAnnotations.init(this, relaxUnitFun = true)
        mockkStatic("com.thomas.test.newsapisample.feature.common.extension.StringExtKt")
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `Given methods call WHEN loading an article THEN verify call order`() {
        val article = Article(
            "author",
            "content",
            "description",
            "2020-04-28T09:25:08Z",
            FAKE_SOURCE,
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
    fun `Given an invalid author name WHEN loading content THEN author is unknown in PO`() {
        // Given
        val article = Article(
            "invalid author",
            "content",
            "description",
            "2020-04-28T09:25:08Z",
            FAKE_SOURCE,
            "title1",
            "url",
            "urlToImage"
        )

        every {
            any<String>().isValidAuthor()
        } returns false

        // When
        articleContentViewModel.loadContent(article)

        // Then
        val author = articleContentViewModel.articleContentLiveData.getOrAwaitValue().author

        assertThat(author).isEqualTo("Unknown")

        verify {
            any<String>().isValidAuthor()
        }
    }

    @Test
    fun `Given a author name WHEN is VALID THEN verify characters`() {
        val author = "Joseph Climber"
        assertThat(author.isValidAuthor()).isTrue()
    }

    @Test
    fun `Given a author name WHEN is INVALID THEN verify characters`() {
        val author = "[Joseph] [Climber]"
        assertThat(author.isValidAuthor()).isFalse()
    }

    companion object {
        val FAKE_SOURCE = ArticleSource(
            id = "id",
            name = "name"
        )
    }
}
