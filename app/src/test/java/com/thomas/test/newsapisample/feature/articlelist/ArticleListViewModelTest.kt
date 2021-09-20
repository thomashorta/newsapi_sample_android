package com.thomas.test.newsapisample.feature.articlelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.ArticleSource
import com.thomas.test.newsapisample.data.model.ArticlesResponse
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.feature.common.NetworkState
import com.thomas.test.newsapisample.getOrAwaitValue
import com.thomas.test.newsapisample.observeValuesForTesting
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArticleListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val testCoroutineDispatcher = TestCoroutineDispatcher()

    @MockK
    lateinit var newsRepositoryMock: NewsRepository

    lateinit var articleListViewModel: ArticleListViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        articleListViewModel = ArticleListViewModel(newsRepositoryMock, testCoroutineDispatcher)
    }

    @Test
    fun `GIVEN null WHEN creating viewmodel without dispatcher THEN uses IO Dispatcher`() {
        // Given

        // When
        val viewModel = ArticleListViewModel(newsRepositoryMock)

        // Then
        TestCase.assertEquals(Dispatchers.IO, viewModel.networkCallContext)
    }

    @Test
    fun `GIVEN repository returns failure and no data was loaded WHEN fetching articles THEN network state is failure`() {
        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        } returns SuspendableResult.error(Exception())

        // When
        articleListViewModel.fetchArticles("")

        // Then
        val networkState = articleListViewModel.networkStateLiveData.getOrAwaitValue()
        TestCase.assertEquals(NetworkState.FAILURE, networkState)
    }

    @Test
    fun `GIVEN repository returns failure and data was loaded WHEN fetching articles THEN network state is idle`() {
        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        }.returnsMany(
            SuspendableResult.of(fakeArticleResponse(FAKE_ARTICLE_LIST_1)),
            SuspendableResult.error(Exception())
        )

        articleListViewModel.fetchArticles("")  // for initialization

        // When
        articleListViewModel.fetchArticles("")

        // Then
        val networkState = articleListViewModel.networkStateLiveData.getOrAwaitValue()
        TestCase.assertEquals(NetworkState.IDLE, networkState)
    }

    @Test
    fun `GIVEN repository returns success and data was not loaded WHEN fetching articles THEN network state is success and article list is received`() {
        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        } returns SuspendableResult.of(fakeArticleResponse(FAKE_ARTICLE_LIST_1))

        // When
        articleListViewModel.fetchArticles("")

        // Then
        val networkState = articleListViewModel.networkStateLiveData.getOrAwaitValue()
        val articles = articleListViewModel.articlesLiveData.getOrAwaitValue()

        TestCase.assertEquals(NetworkState.SUCCESS, networkState)
        TestCase.assertEquals(FAKE_ARTICLE_LIST_1, articles)
    }

    @Test
    fun `GIVEN repository returns success and data was loaded WHEN fetching articles THEN network state is success and article list has all articles`() {
        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        }.returnsMany(
            SuspendableResult.of(fakeArticleResponse(FAKE_ARTICLE_LIST_1)),
            SuspendableResult.of(fakeArticleResponse(FAKE_ARTICLE_LIST_2))
        )

        articleListViewModel.fetchArticles("")  // for initialization

        // When
        articleListViewModel.fetchArticles("")

        // Then
        val networkState = articleListViewModel.networkStateLiveData.getOrAwaitValue()
        val articles = articleListViewModel.articlesLiveData.getOrAwaitValue()

        TestCase.assertEquals(NetworkState.SUCCESS, networkState)
        TestCase.assertEquals(FAKE_ARTICLE_LIST_1 + FAKE_ARTICLE_LIST_2, articles)
    }

    @Test
    fun `GIVEN repository has enough articles WHEN fetching articles twice THEN second request gets page 2`() {
        // Given
        val pageSlot = slot<Int>()
        coEvery {
            newsRepositoryMock.getEverything(any(), capture(pageSlot), any())
        } coAnswers {
            SuspendableResult.of {
                when (pageSlot.captured) {
                    1 -> fakeArticleResponse(FAKE_ARTICLE_LIST_1)
                    2 -> fakeArticleResponse(FAKE_ARTICLE_LIST_2)
                    else -> throw Exception()
                }
            }
        }

        articleListViewModel.fetchArticles("")  // for initialization

        // When
        articleListViewModel.fetchArticles("")

        // Then
        TestCase.assertEquals(2, pageSlot.captured)

        // another way (verifying the whole sequence of calls)
        coVerifyOrder {
            newsRepositoryMock.getEverything(any(), 1, any())
            newsRepositoryMock.getEverything(any(), 2, any())
        }
    }

    @Test
    fun `GIVEN repository returns same data WHEN fetching articles THEN network state is success and article list is received without duplicates`() {
        val viewModelSpy = spyk(articleListViewModel)

        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        } returns SuspendableResult.of(fakeArticleResponse(FAKE_ARTICLE_LIST_1))

        // When
        viewModelSpy.fetchArticles("")
        viewModelSpy.fetchArticles("")

        // Then
        val networkState = viewModelSpy.networkStateLiveData.getOrAwaitValue()
        val articles = viewModelSpy.articlesLiveData.getOrAwaitValue()

        TestCase.assertEquals(NetworkState.SUCCESS, networkState)
        TestCase.assertEquals(FAKE_ARTICLE_LIST_1, articles)

        // line below is unnecessary for testing it and can cause failure on internal logic refactor:
        verify(exactly = 1) {
            viewModelSpy.removeDuplicates(FAKE_ARTICLE_LIST_1 + FAKE_ARTICLE_LIST_1)
        }
    }

    @Test
    fun `GIVEN repository returns anything WHEN fetching articles THEN network state should go through loading`() {
        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        }.returnsMany(
            SuspendableResult.error(Exception()),
            SuspendableResult.of(fakeArticleResponse(FAKE_ARTICLE_LIST_1))
        )

        // When
        val statesFailure = articleListViewModel.networkStateLiveData.observeValuesForTesting {
            articleListViewModel.fetchArticles("")
        }
        val statesSuccess = articleListViewModel.networkStateLiveData.observeValuesForTesting {
            articleListViewModel.fetchArticles("")
        }

        // Then
        TestCase.assertTrue(statesFailure.contains(NetworkState.LOADING))
        TestCase.assertTrue(statesSuccess.contains(NetworkState.LOADING))
    }

    @Test
    fun `GIVEN view model WHEN fetching articles THEN only call getEverything and not getSources`() {
        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        } returns SuspendableResult.error(Exception()) // could return anything

        coEvery {
            newsRepositoryMock.getSources(any(), any())
        } returns SuspendableResult.error(Exception()) // could return anything

        // When
        articleListViewModel.fetchArticles("")

        // Then
        coVerify {
            newsRepositoryMock.getSources(any(), any()) wasNot called
            newsRepositoryMock.getEverything(any(), any(), any())
        }
    }

    @Test
    fun `GIVEN view model WHEN fetching articles twice in a short time THEN only one call should be made`() {
        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        } coAnswers {
            delay(1000)
            SuspendableResult.error(Exception())
        }

        // When
        articleListViewModel.fetchArticles("")
        testCoroutineDispatcher.advanceTimeBy(500)
        articleListViewModel.fetchArticles("")
        testCoroutineDispatcher.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) {
            newsRepositoryMock.getEverything(any(), any(), any())
        }
    }

    /**
     * Other way for test above, but considering only second call as the main test case
     */
    @Test
    fun `GIVEN view model WHEN fetching articles again in a short time THEN it should not call repository`() {
        // Given
        coEvery {
            newsRepositoryMock.getEverything(any(), any(), any())
        } coAnswers {
            delay(1000)
            SuspendableResult.error(Exception())
        }

        // make first call and keep it ongoing (paused dispatcher)
        articleListViewModel.fetchArticles("")
        testCoroutineDispatcher.advanceTimeBy(500)

        // clear initial record calls
        clearMocks(
            newsRepositoryMock,
            answers = false
        )

        // When
        articleListViewModel.fetchArticles("")
        testCoroutineDispatcher.advanceUntilIdle()

        // Then
        coVerify {
            newsRepositoryMock wasNot called
        }
    }

    companion object {
        val FAKE_SOURCE = ArticleSource(
            id = "id",
            name = "name"
        )

        val FAKE_ARTICLE_LIST_1 = arrayListOf(
            Article(
                "author",
                "content",
                "description",
                "publishedAt",
                FAKE_SOURCE,
                "title1",
                "url",
                "urlToImage"
            ),
            Article(
                "author",
                "content",
                "description",
                "publishedAt",
                FAKE_SOURCE,
                "title2",
                "url",
                "urlToImage"
            ),
            Article(
                "author",
                "content",
                "description",
                "publishedAt",
                FAKE_SOURCE,
                "title3",
                "url",
                "urlToImage"
            )
        )

        val FAKE_ARTICLE_LIST_2 = arrayListOf(
            Article(
                "author",
                "content",
                "description",
                "publishedAt",
                FAKE_SOURCE,
                "title4",
                "url",
                "urlToImage"
            ),
            Article(
                "author",
                "content",
                "description",
                "publishedAt",
                FAKE_SOURCE,
                "title5",
                "url",
                "urlToImage"
            ),
            Article(
                "author",
                "content",
                "description",
                "publishedAt",
                FAKE_SOURCE,
                "title6",
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

}