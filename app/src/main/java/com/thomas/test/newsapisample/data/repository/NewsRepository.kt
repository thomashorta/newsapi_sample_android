package com.thomas.test.newsapisample.data.repository

import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.data.model.ArticlesResponse
import com.thomas.test.newsapisample.data.model.SourceResponse

interface NewsRepository {

    suspend fun getSources(
        country: String?,
        category: String?
    ): SuspendableResult<SourceResponse, Exception>

    suspend fun getEverything(
        sources: String?,
        page: Int = 1,
        pageSize: Int = 20
    ): SuspendableResult<ArticlesResponse, Exception>
}
