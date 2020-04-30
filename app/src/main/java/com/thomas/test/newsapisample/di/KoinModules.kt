package com.thomas.test.newsapisample.di

import com.thomas.test.newsapisample.data.EndpointService
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.data.repository.NewsRepositoryImpl
import com.thomas.test.newsapisample.feature.articlecontent.ArticleContentViewModel
import com.thomas.test.newsapisample.feature.articlelist.ArticleListViewModel
import com.thomas.test.newsapisample.feature.sourcelist.SourceListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.text.SimpleDateFormat
import java.util.*
import kotlin.contracts.ExperimentalContracts

val serviceModule = module {
    single { EndpointService() }
    single(named("ApiDateFormat")) {
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }
}

val repositoryModule = module {
    single<NewsRepository> { NewsRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { SourceListViewModel(get()) }
    viewModel { ArticleListViewModel(get()) }
    viewModel { ArticleContentViewModel(get(named("ApiDateFormat"))) }
}

val appModules = listOf(serviceModule, repositoryModule, viewModelModule)