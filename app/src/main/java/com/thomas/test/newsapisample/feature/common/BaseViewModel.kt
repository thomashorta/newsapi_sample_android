package com.thomas.test.newsapisample.feature.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    protected val _networkStateLiveData = MutableLiveData(NetworkState.IDLE)
    val networkStateLiveData: LiveData<NetworkState>
        get() = _networkStateLiveData
}
