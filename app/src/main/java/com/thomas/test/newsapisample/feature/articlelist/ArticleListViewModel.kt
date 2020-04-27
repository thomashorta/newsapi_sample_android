package com.thomas.test.newsapisample.feature.articlelist

import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.feature.common.BaseViewModel
import com.thomas.test.newsapisample.feature.common.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ArticleListViewModel(
    private val newsRepository: NewsRepository,
    @VisibleForTesting
    internal val networkCallContext: CoroutineContext = Dispatchers.IO
) : BaseViewModel() {

    private var currentPage = 1
    private val _articlesLiveData = MutableLiveData<List<Article>>()
    val articlesLiveData: LiveData<List<Article>>
        get() = _articlesLiveData

    fun fetchArticles(sourceId: String) {
        if (_networkStateLiveData.value?.isLoading() == true) {
            // already fetching results
            return
        }

        if (_articlesLiveData.value?.isEmpty() == false) {
            _networkStateLiveData.value = NetworkState.INTERNAL_LOADING
        } else {
            _networkStateLiveData.value = NetworkState.LOADING
        }

        viewModelScope.launch(networkCallContext) {
            when (val result = newsRepository.getEverything(sourceId, currentPage)) {
                is SuspendableResult.Success -> {
                    val list: MutableList<Article> = _articlesLiveData.value?.let {
                        mutableListOf(*it.toTypedArray())
                    } ?: mutableListOf()
                    list.addAll(result.value.articles)

                    currentPage++
                    _articlesLiveData.postValue(removeDuplicates(list))
                    _networkStateLiveData.postValue(NetworkState.SUCCESS)
                }
                is SuspendableResult.Failure -> {
                    Log.e("ArticleList", "Error fetching from $sourceId", result.error)
                    if (_articlesLiveData.value?.isEmpty() == false) {
                        _networkStateLiveData.postValue(NetworkState.IDLE)
                    } else {
                        _networkStateLiveData.postValue(NetworkState.FAILURE)
                    }
                }
            }
        }
    }

    // TODO usar isso para exemplo de codigo refatorado quebrando no teste
    @VisibleForTesting
    internal fun removeDuplicates(list: List<Article>): List<Article> {
        return LinkedHashSet<Article>().run {
            addAll(list)
            toList()
        }
    }

}
