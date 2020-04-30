package com.thomas.test.newsapisample.feature.common.extension

import kotlin.contracts.contract

fun String.containsAny(characters: List<String>) = characters.find { contains(it) } != null

fun String?.isValidAuthor(): Boolean {
    contract {
        returns(true) implies (this@isValidAuthor != null)
    }

    if (this == null) return false
    return isNotEmpty() && !containsAny(INVALID_AUTHOR_CHARACTERS)
}

internal val INVALID_AUTHOR_CHARACTERS = listOf("[", "]", "<", ">", "{", "}")