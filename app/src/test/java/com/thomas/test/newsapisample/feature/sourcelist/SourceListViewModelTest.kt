package com.thomas.test.newsapisample.feature.sourcelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.data.model.SourceResponse
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.feature.common.NetworkState
import com.thomas.test.newsapisample.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

class SourceListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var newsRepositoryMock: NewsRepository

    lateinit var sourceListViewModel: SourceListViewModel

    val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        sourceListViewModel = SourceListViewModel(newsRepositoryMock, testCoroutineDispatcher)
    }

    @Test
    fun `GIVEN null WHEN creating viewModel without dispatcher THEN uses IO Dispatcher`() {
        // Given

        // When
        val viewModel = SourceListViewModel(newsRepositoryMock)

        // Then
        TestCase.assertEquals(Dispatchers.IO, viewModel.networkCallContext)
    }

    @Test
    fun `GIVEN repository returns failure WHEN fetch sources THEN nothing is loaded`() {
        // Given
        coEvery {
            newsRepositoryMock.getSources(any(), any())
        } returns SuspendableResult.error(Exception())

        // When
        sourceListViewModel.fetchSources()

        // Then
        val networkState = sourceListViewModel.networkStateLiveData.getOrAwaitValue()
        TestCase.assertEquals(NetworkState.FAILURE, networkState)
        TestCase.assertEquals(1, sourceListViewModel.errorCount)
    }

    @Test
    fun `GIVEN repository returns failure WHEN fetch sources for 3 times THEN nothing is loaded and state is IDLE`() {
        // Given
        coEvery {
            newsRepositoryMock.getSources(any(), any())
        } returns SuspendableResult.error(Exception())

        // When
        sourceListViewModel.fetchSources()
        sourceListViewModel.fetchSources()
        sourceListViewModel.fetchSources()
        sourceListViewModel.fetchSources()

        // Then
        val networkState = sourceListViewModel.networkStateLiveData.getOrAwaitValue()
        TestCase.assertEquals(NetworkState.IDLE, networkState)
        TestCase.assertEquals(3, sourceListViewModel.errorCount)
        coVerify(exactly = 4) { newsRepositoryMock.getSources(null, null) }
    }

    @Test
    fun `GIVEN repository returns success WHEN fetch sources for 3 times THEN 2 first returns failure`() {
        // Given
        val fakeResponse = fakeListOfSources()
        coEvery {
            newsRepositoryMock.getSources(any(), any())
        }.returnsMany(
            SuspendableResult.error(Exception()),
            SuspendableResult.error(Exception()),
            SuspendableResult.of(fakeResponse)
        )

        // When
        sourceListViewModel.fetchSources()
        sourceListViewModel.fetchSources()
        sourceListViewModel.fetchSources()

        // Then
        val networkState = sourceListViewModel.networkStateLiveData.getOrAwaitValue()
        TestCase.assertEquals(NetworkState.SUCCESS, networkState)
        TestCase.assertEquals(0, sourceListViewModel.errorCount)
        TestCase.assertEquals(fakeResponse.sources ,sourceListViewModel.sourcesLiveData.value)
        coVerify(exactly = 3) { newsRepositoryMock.getSources(null, null) }
    }

    companion object {
        fun fakeListOfSources() = SourceResponse(listOf(
            Source(
                "category",
                "country",
                "description",
                "id",
                "language",
                "name",
                "url"
            )
        ), "status")
    }
}