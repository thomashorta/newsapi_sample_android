package com.thomas.test.newsapisample.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.google.common.truth.Truth.assertThat
import com.thomas.test.newsapisample.data.EndpointService
import com.thomas.test.newsapisample.data.endpoint.NewsEndpoint
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.ArticlesResponse
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.data.model.SourceResponse
import com.thomas.test.newsapisample.feature.articlelist.ArticleListViewModelTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.exp

class NewsRepositoryImplTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var mockNewsEndpoint: NewsEndpoint

    @MockK
    lateinit var mockEndpointService: EndpointService

    lateinit var repository: NewsRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockEndpointService()
        Dispatchers.setMain(testCoroutineDispatcher)
        repository = NewsRepositoryImpl(mockEndpointService)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    private fun mockEndpointService() {
        every {
            mockEndpointService[any<Class<NewsEndpoint>>()]
        } returns mockNewsEndpoint
    }

    @Test
    fun `GIVEN error WHEN getting sources THEN return error result`() {
        runBlockingTest(testCoroutineDispatcher) {
            // GIVEN
            coEvery {
                mockNewsEndpoint.getSources(any(), any())
            } throws FAKE_ERROR

            // WHEN
            val result = repository.getSources(null, null)

            // THEN
            assertThat(result).isInstanceOf(SuspendableResult.Failure::class.java)
            assertThat(result.component2()).isEqualTo(FAKE_ERROR)
        }
    }

    @Test
    fun `GIVEN success WHEN getting sources THEN return source response success`() {
        runBlockingTest(testCoroutineDispatcher) {
            // GIVEN
            coEvery {
                mockNewsEndpoint.getSources(any(), any())
            } returns FAKE_SOURCE_RESPONSE

            // WHEN
            val result = repository.getSources(null, null)

            // THEN
            assertThat(result).isInstanceOf(SuspendableResult.Success::class.java)
            assertThat(result.component1()).isEqualTo(FAKE_SOURCE_RESPONSE)
        }
    }

    @Test
    fun `GIVEN error WHEN getting everything THEN return error result`() {
        runBlockingTest(testCoroutineDispatcher) {
            // GIVEN
            coEvery {
                mockNewsEndpoint.getEverything(any(), any(), any())
            } throws FAKE_ERROR

            // WHEN
            val result = repository.getEverything(null, 0, 0)

            // THEN
            assertThat(result).isInstanceOf(SuspendableResult.Failure::class.java)
            assertThat(result.component2()).isEqualTo(FAKE_ERROR)
        }
    }

    @Test
    fun `GIVEN success WHEN getting everything THEN return article response success`() {
        runBlockingTest(testCoroutineDispatcher) {
            val expected = fakeArticleResponse(FAKE_ARTICLE_LIST_1)
            // GIVEN
            coEvery {
                mockNewsEndpoint.getEverything(any(), any(), any())
            } returns expected

            // WHEN
            val result = repository.getEverything(null, 0, 3)

            // THEN
            assertThat(result).isInstanceOf(SuspendableResult.Success::class.java)
            assertThat(result.component1()).isEqualTo(expected)
        }
    }

    companion object {
        private val FAKE_ERROR = Exception("error")

        val FAKE_SOURCE_RESPONSE = SourceResponse(
            listOf(
                Source(
                    "category",
                    "country",
                    "description",
                    "id",
                    "language",
                    "name",
                    "url"
                )
            ),
            "status"
        )
    }

    val FAKE_ARTICLE_LIST_1 = arrayListOf(
        Article(
            "author",
            "content",
            "description",
            "publishedAt",
            ArticleListViewModelTest.FAKE_SOURCE,
            "title1",
            "url",
            "urlToImage"
        ),
        Article(
            "author",
            "content",
            "description",
            "publishedAt",
            ArticleListViewModelTest.FAKE_SOURCE,
            "title2",
            "url",
            "urlToImage"
        ),
        Article(
            "author",
            "content",
            "description",
            "publishedAt",
            ArticleListViewModelTest.FAKE_SOURCE,
            "title3",
            "url",
            "urlToImage"
        )
    )

    fun fakeArticleResponse(articles: ArrayList<Article>) = ArticlesResponse(
        status = "200",
        totalResults = articles.size,
        articles = articles
    )
}