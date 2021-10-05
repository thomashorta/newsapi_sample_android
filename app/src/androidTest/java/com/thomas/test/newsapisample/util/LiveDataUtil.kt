package com.thomas.test.newsapisample.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.mockk.every

inline fun <reified T> mockkLiveData(crossinline provider: () -> LiveData<T>): MutableLiveData<T> {
    val mockLiveData = MutableLiveData<T>()
    every { provider() } returns mockLiveData
    return mockLiveData
}
