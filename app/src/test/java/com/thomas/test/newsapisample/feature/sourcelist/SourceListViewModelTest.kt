package com.thomas.test.newsapisample.feature.sourcelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.google.common.truth.Truth.assertThat
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.data.model.SourceResponse
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.feature.common.NetworkState
import com.thomas.test.newsapisample.getOrAwaitValue
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.After
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

    private val testCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        sourceListViewModel = SourceListViewModel(newsRepositoryMock, testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `WHEN creating viewModel without dispatcher THEN uses IO Dispatcher`() {
        // Given

        // When
        val viewModel = SourceListViewModel(newsRepositoryMock)

        // Then
        assertThat(viewModel.networkCallContext).isEqualTo(Dispatchers.IO)
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
        assertThat(networkState).isEqualTo(NetworkState.FAILURE)
        assertThat(sourceListViewModel.errorCount).isEqualTo(1)
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
        assertThat(networkState).isEqualTo(NetworkState.IDLE)
        assertThat(sourceListViewModel.errorCount).isEqualTo(3)
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
        assertThat(networkState).isEqualTo(NetworkState.SUCCESS)
        assertThat(sourceListViewModel.errorCount).isEqualTo(0)
        assertThat(sourceListViewModel.sourcesLiveData.value).isEqualTo(fakeResponse.sources)
        coVerify(exactly = 3) { newsRepositoryMock.getSources(null, null) }
    }

    companion object {
        fun fakeListOfSources() = SourceResponse(
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
}
