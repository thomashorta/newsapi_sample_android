package com.thomas.test.newsapisample.feature.common.extension

fun String.containsAny(characters: List<String>) = characters.find { contains(it) } != null
