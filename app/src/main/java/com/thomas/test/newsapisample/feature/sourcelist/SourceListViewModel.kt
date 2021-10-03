package com.thomas.test.newsapisample.feature.sourcelist

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.feature.common.BaseViewModel
import com.thomas.test.newsapisample.feature.common.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SourceListViewModel(
    private val newsRepository: NewsRepository,
    @VisibleForTesting
    internal val networkCallContext: CoroutineContext = Dispatchers.IO
) : BaseViewModel() {

    private val _sourcesLiveData = MutableLiveData<List<Source>>()

    @VisibleForTesting
    internal var errorCount = 0
    val sourcesLiveData: LiveData<List<Source>>
        get() = _sourcesLiveData

    fun fetchSources() {
        _networkStateLiveData.value = NetworkState.LOADING
        viewModelScope.launch(networkCallContext) {
            when (val result = newsRepository.getSources(null, null)) {
                is SuspendableResult.Success -> {
                    _sourcesLiveData.postValue(result.value.sources)
                    _networkStateLiveData.postValue(NetworkState.SUCCESS)
                    errorCount = 0
                }
                is SuspendableResult.Failure -> {
                    if (errorCount > 2) {
                        _networkStateLiveData.postValue(NetworkState.IDLE)
                        return@launch
                    }
                    errorCount++
                    _networkStateLiveData.postValue(NetworkState.FAILURE)
                }
            }
        }
    }
}
