package com.thomas.test.newsapisample.di

import com.thomas.test.newsapisample.data.EndpointService
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.data.repository.NewsRepositoryImpl
import com.thomas.test.newsapisample.feature.articlelist.ArticleListViewModel
import com.thomas.test.newsapisample.feature.sourcelist.SourceListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val serviceModule = module {
    single { EndpointService() }
}

val repositoryModule = module {
    single<NewsRepository> { NewsRepositoryImpl(get()) }
}

val viewModelModule = module {
    viewModel { SourceListViewModel(get()) }
    viewModel { ArticleListViewModel(get()) }
}

val appModules = listOf(serviceModule, repositoryModule, viewModelModule)