package com.thomas.test.newsapisample.data.endpoint

import com.thomas.test.newsapisample.data.model.ArticlesResponse
import com.thomas.test.newsapisample.data.model.SourceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsEndpoint {
    @GET("/v2/sources")
    suspend fun getSources(
        @Query("country") country: String?,
        @Query("category") category: String?
    ): SourceResponse

    @GET("/v2/everything")
    suspend fun getEverything(
        @Query("sources") sources: String?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): ArticlesResponse
}