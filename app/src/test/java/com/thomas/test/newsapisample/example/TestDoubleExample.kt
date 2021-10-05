package com.thomas.test.newsapisample.example

import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.ArticlesResponse
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.data.model.SourceResponse
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.util.ArticleFactory
import com.thomas.test.newsapisample.util.SourceFactory
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk

class NewsRepositoryDummy : NewsRepository {
    override suspend fun getSources(
        country: String?,
        category: String?
    ): SuspendableResult<SourceResponse, Exception> {
        TODO("no-op")
    }

    override suspend fun getEverything(
        sources: String?,
        page: Int,
        pageSize: Int
    ): SuspendableResult<ArticlesResponse, Exception> {
        TODO("no-op")
    }
}

class NewsRepositoryStub : NewsRepository {
    override suspend fun getSources(
        country: String?,
        category: String?
    ): SuspendableResult<SourceResponse, Exception> {
        return SuspendableResult.of {
            SourceResponse(
                status = "",
                sources = listOf(
                    SourceFactory.createSource("one"),
                    SourceFactory.createSource("two"),
                )
            )
        }
    }

    override suspend fun getEverything(
        sources: String?,
        page: Int,
        pageSize: Int
    ): SuspendableResult<ArticlesResponse, Exception> {
        return SuspendableResult.of {
            ArticlesResponse(
                status = "",
                totalResults = 3,
                articles = arrayListOf(
                    ArticleFactory.createArticle("one"),
                    ArticleFactory.createArticle("two"),
                    ArticleFactory.createArticle("three"),
                )
            )
        }
    }
}

class NewsRepositoryFake : NewsRepository {
    val sourcesList = listOf<Source>()
    val articleList = arrayListOf<Article>()

    override suspend fun getSources(
        country: String?,
        category: String?
    ): SuspendableResult<SourceResponse, Exception> {
        return SuspendableResult.of {
            SourceResponse(
                status = "",
                sources = sourcesList
            )
        }
    }

    override suspend fun getEverything(
        sources: String?,
        page: Int,
        pageSize: Int
    ): SuspendableResult<ArticlesResponse, Exception> {
        return SuspendableResult.of {
            ArticlesResponse(
                status = "",
                totalResults = 3,
                articles = articleList
            )
        }
    }
}

class MockkRepositories {
    private val newsRepositoryMock: NewsRepository = mockk() {
        coEvery { getSources(any(), any()) } coAnswers {
            SuspendableResult.of {
                SourceResponse(
                    status = "",
                    sources = listOf(
                        SourceFactory.createSource("one"),
                        SourceFactory.createSource("two"),
                    )
                )
            }
        }
    }
    private val newsRepositorySpy: NewsRepository = spyk(NewsRepositoryFake())
}
