package com.thomas.test.newsapisample.feature.sourcelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.thomas.test.newsapisample.data.model.Source
import com.thomas.test.newsapisample.data.repository.NewsRepository
import com.thomas.test.newsapisample.feature.common.BaseViewModel
import com.thomas.test.newsapisample.feature.common.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SourceListViewModel(
    private val newsRepository: NewsRepository
) : BaseViewModel() {

    private val _sourcesLiveData = MutableLiveData<List<Source>>()
    val sourcesLiveData: LiveData<List<Source>>
        get() = _sourcesLiveData

    fun fetchSources() {
        _networkStateLiveData.value = NetworkState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = newsRepository.getSources(null, null)) {
                is SuspendableResult.Success -> {
                    _sourcesLiveData.postValue(result.value.sources)
                    _networkStateLiveData.postValue(NetworkState.SUCCESS)
                }
                is SuspendableResult.Failure -> {
                    _networkStateLiveData.postValue(NetworkState.FAILURE)
                }
            }
        }
    }

}
