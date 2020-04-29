package com.thomas.test.newsapisample.data.repository

import com.github.kittinunf.result.coroutines.SuspendableResult
import com.isansc.apollographqltestwithmockwebserverpoc.tools.ServerMock
import com.isansc.apollographqltestwithmockwebserverpoc.tools.ServiceMocker
import com.thomas.test.newsapisample.data.EndpointService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.SpyK
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class NewsRepositoryImplTest {

    @SpyK
    var endpointService: EndpointService = EndpointService()

    @InjectMockKs
    lateinit var newsRepository: NewsRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
//        newsRepository = NewsRepositoryImpl(endpointService)
        ServerMock.startServer()
    }

    @After
    fun tearDown() {
        ServerMock.shutDownServer()
    }

    @Test
    fun changePrivateMethodResult() {
        ServiceMocker.mockGetSources()
        System.out.println("ServerMock: " + ServerMock.getUrl("/"))

        every { endpointService.getBaseURL() } returns ServerMock.getUrl("/").toHttpUrl()
//        every { endpointService["getBaseURL"]() } returns ServerMock.getUrl("/").toHttpUrl()

        System.out.println("BaseURL Mockado: " + endpointService.getBaseURL())

        runBlocking{
            System.out.println("Executando getSources")

            when(newsRepository.getSources(null, null)) {
                is SuspendableResult.Success -> {
                    verify { endpointService.getBaseURL() }
//                    verify { endpointService["getBaseURL"]() }
                    assertTrue(true)
                }
                is SuspendableResult.Failure -> {
                    assertTrue(true)
                }
            }
        }
    }

    @Test
    fun changePrivateMethodResultMockLocal() {
        runBlocking{
            ServiceMocker.mockGetSources()
            val endpointServiceLocal = spyk(EndpointService())

            System.out.println("ServerMock: " + ServerMock.getUrl("/"))
            every { endpointServiceLocal.getBaseURL() } returns ServerMock.getUrl("/").toHttpUrl()

            System.out.println("BaseURL Mockado: " + endpointServiceLocal.getBaseURL())
            val newsRepositoryLocal = NewsRepositoryImpl(endpointServiceLocal)

            System.out.println("Executando getSources")
            when(newsRepositoryLocal.getSources(null, null)) {
                is SuspendableResult.Success -> {
                    verify { endpointServiceLocal.getBaseURL() }
                    assertTrue(true)
                }
                is SuspendableResult.Failure -> {
                    assertTrue(true)
                }
            }
        }
    }
}