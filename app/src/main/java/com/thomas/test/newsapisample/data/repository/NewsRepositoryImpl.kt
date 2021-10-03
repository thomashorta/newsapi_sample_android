package com.thomas.test.newsapisample.data.repository

import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.data.EndpointService
import com.thomas.test.newsapisample.data.Repository
import com.thomas.test.newsapisample.data.endpoint.NewsEndpoint
import com.thomas.test.newsapisample.data.model.ArticlesResponse
import com.thomas.test.newsapisample.data.model.SourceResponse

class NewsRepositoryImpl(
    endpointService: EndpointService
) : Repository<NewsEndpoint>(endpointService), NewsRepository {

    override suspend fun getSources(
        country: String?,
        category: String?
    ) = SuspendableResult.of<SourceResponse, Exception> {
        getEndpoint().getSources(country, category)
    }

    override suspend fun getEverything(
        sources: String?,
        page: Int,
        pageSize: Int
    ) = SuspendableResult.of<ArticlesResponse, Exception> {
        getEndpoint().getEverything(sources, page, pageSize)
    }
}
