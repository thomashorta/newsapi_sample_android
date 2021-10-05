package com.thomashorta.test.newsapisample.gradleconfig

object Libs {

    object Core {
        val kotlin_stdlib_jdk7 = lib(
            "org.jetbrains.kotlin:kotlin-stdlib-jdk7",
            Versions.kotlin
        )

        val appcompat = lib(
            "androidx.appcompat:appcompat",
            Versions.appcompat
        )

        val core_ktx = lib(
            "androidx.core:core-ktx",
            Versions.core_ktx
        )

        val coroutines_core = lib(
            "org.jetbrains.kotlinx:kotlinx-coroutines-core",
            Versions.coroutines
        )

        val coroutines_android = lib(
            "org.jetbrains.kotlinx:kotlinx-coroutines-android",
            Versions.coroutines
        )

        val legacy = lib(
            "androidx.legacy:legacy-support-v4",
            Versions.legacy
        )

        val lifecycle_extensions = lib(
            "androidx.lifecycle:lifecycle-extensions",
            Versions.lifecycle
        )

        val lifecycle_viewmodel = lib(
            "androidx.lifecycle:lifecycle-viewmodel-ktx",
            Versions.lifecycle
        )
    }

    object DI {
        val koin_android = lib(
            "io.insert-koin:koin-android",
            Versions.koin
        )
    }

    object Data {
        val gson = lib(
            "com.google.code.gson:gson",
            Versions.gson
        )

        val okhttp = lib(
            "com.squareup.okhttp3:okhttp",
            Versions.okhttp
        )

        val retrofit = lib(
            "com.squareup.retrofit2:retrofit",
            Versions.retrofit
        )

        val retrofit_converter_gson = lib(
            "com.squareup.retrofit2:converter-gson",
            Versions.retrofit
        )

        val logging_interceptor = lib(
            "com.squareup.okhttp3:logging-interceptor",
            Versions.okhttp
        )

        val result = lib(
            "com.github.kittinunf.result:result",
            Versions.kittinunf_result
        )

        val result_coroutines = lib(
            "com.github.kittinunf.result:result-coroutines",
            Versions.kittinunf_result
        )
    }

    object UI {
        val constraintlayout = lib(
            "androidx.constraintlayout:constraintlayout",
            Versions.constraint_layout
        )

        val material = lib(
            "com.google.android.material:material",
            Versions.material
        )

        val recyclerView = lib(
            "androidx.recyclerview:recyclerview",
            Versions.recyclerview
        )

        val coil = lib(
            "io.coil-kt:coil",
            Versions.coil
        )
    }

    object Test {
        val junit = lib(
            "junit:junit",
            Versions.junit
        )

        val junit_androidx_ext = lib(
            "androidx.test.ext:junit",
            Versions.junit_androidx_ext
        )

        val espresso_core = lib(
            "androidx.test.espresso:espresso-core",
            Versions.espresso
        )

        val espresso_contrib = lib(
            "androidx.test.espresso:espresso-contrib",
            Versions.espresso
        )

        val espresso_intents = lib(
            "androidx.test.espresso:espresso-intents",
            Versions.espresso
        )

        val okhttp_mockwebserver = lib(
            "com.squareup.okhttp3:mockwebserver",
            Versions.okhttp
        )

        val coroutines_test = lib(
            "org.jetbrains.kotlinx:kotlinx-coroutines-test",
            Versions.coroutines
        )

        val mockk = lib(
            "io.mockk:mockk",
            Versions.mockk
        )

        val mockkAndroid = lib(
            "io.mockk:mockk-android",
            Versions.mockk
        )

        val core_testing = lib(
            "android.arch.core:core-testing",
            Versions.core_testing
        )

        val truth = lib(
            "com.google.truth:truth",
            Versions.truth
        )

        val fragment = lib(
            "androidx.fragment:fragment-testing",
            Versions.fragment
        )

        val test_runner = lib(
            "androidx.test:runner",
            Versions.test
        )

        val test_rules = lib(
            "androidx.test:rules",
            Versions.test
        )

        val robolectric = lib(
            "org.robolectric:robolectric",
            Versions.robolectric
        )
    }

    object Plugins {
        val gradle = lib(
            "com.android.tools.build:gradle",
            Versions.gradle
        )

        val kotlin = lib(
            "org.jetbrains.kotlin:kotlin-gradle-plugin",
            Versions.kotlin
        )

        val gradle_versions = lib(
            "com.github.ben-manes:gradle-versions-plugin",
            Versions.gradle_versions
        )
    }

    private fun lib(path: String, version: String) = "$path:$version"
}
