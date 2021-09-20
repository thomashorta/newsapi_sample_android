package com.thomas.test.newsapisample.util

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment

fun <T : View> Activity.bind(idResolver: () -> Int): Lazy<T> = lazy {
    findViewById(idResolver())
}

fun <T : View> Fragment.bind(idResolver: () -> Int): Lazy<T> = lazy {
    requireView().findViewById(idResolver())
}

fun <T : View> View.bind(idResolver: () -> Int): Lazy<T> = lazy {
    findViewById(idResolver())
}
