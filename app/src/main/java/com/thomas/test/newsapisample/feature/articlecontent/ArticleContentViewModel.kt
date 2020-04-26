package com.thomas.test.newsapisample.feature.articlecontent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thomas.test.newsapisample.data.model.Article
import com.thomas.test.newsapisample.data.model.ArticlePO
import com.thomas.test.newsapisample.feature.common.BaseViewModel
import com.thomas.test.newsapisample.feature.common.NetworkState
import com.thomas.test.newsapisample.feature.common.extension.containsAny
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ArticleContentViewModel(
    private val apiDateFormat: SimpleDateFormat
) : BaseViewModel() {

    private val _articleContentLiveData = MutableLiveData<ArticlePO>()
    val articleContentLiveData: LiveData<ArticlePO>
        get() = _articleContentLiveData

    fun loadContent(article: Article) {
        val content = transformContent(article.content)
        val author = transformAuthor(article.author)
        val timeSincePublish = transformPublishTime(article.publishedAt)

        _networkStateLiveData.postValue(NetworkState.SUCCESS)
        _articleContentLiveData.postValue(
            ArticlePO(
            article.title,
            article.description,
            content,
            author,
            article.url,
            article.urlToImage,
            timeSincePublish
        )
        )
    }

    private fun transformContent(content: String): String {
        return content.trim()
            .takeIf { it.endsWith(WORD_COUNT_BLOCK_END) }
            ?.substringBefore(WORD_COUNT_BLOCK_START)
            ?: content.trim()
    }

    private fun transformAuthor(author: String?): String {
        return if (author.isNullOrEmpty() || author.containsAny(INVALID_AUTHOR_CHARACTERS)) {
            UNKNOWN_AUTHOR
        } else {
            author
        }
    }

    private fun transformPublishTime(publishedAt: String): String? {
        if (publishedAt.isEmpty()) return null

        return apiDateFormat.parse(publishedAt)?.time?.let { publishedTime ->
            val today = Calendar.getInstance().timeInMillis
            val diffInMillis = today - publishedTime

            val diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
            val diff = when {
                diffInDays >= YEAR_IN_DAYS -> {
                    val years = diffInDays / YEAR_IN_DAYS
                    DateTimeDifference(
                        time = years,
                        unit = if (years > 1) "years" else "year"
                    )
                }
                diffInDays >= MONTH_IN_DAYS -> {
                    val months = diffInDays / MONTH_IN_DAYS
                    DateTimeDifference(
                        time = months,
                        unit = if (months > 1) "months" else "month"
                    )
                }
                diffInDays >= WEEK_IN_DAYS -> {
                    val weeks = diffInDays / WEEK_IN_DAYS
                    DateTimeDifference(
                        time = weeks,
                        unit = if (weeks > 1) "weeks" else "week"
                    )
                }
                diffInDays > 0 -> {
                    DateTimeDifference(
                        time = diffInDays,
                        unit = if (diffInDays > 1) "days" else "day"
                    )
                }
                else -> {
                    val diffInSeconds = TimeUnit.SECONDS.convert(diffInMillis, TimeUnit.MILLISECONDS)
                    when {
                        diffInSeconds >= HOUR_IN_SECONDS -> {
                            val hours = diffInSeconds / HOUR_IN_SECONDS
                            DateTimeDifference(
                                time = hours,
                                unit = if (hours > 1) "hours" else "hour"
                            )
                        }
                        diffInSeconds >= MINUTE_IN_SECONDS -> {
                            val hours = diffInSeconds / MINUTE_IN_SECONDS
                            DateTimeDifference(
                                time = hours,
                                unit = if (hours > 1) "minutes" else "minute"
                            )
                        }
                        else -> {
                            DateTimeDifference(
                                time = diffInSeconds,
                                unit = if (diffInSeconds > 1) "seconds" else "second"
                            )
                        }
                    }
                }
            }

            diff.formatted
        }
    }

    private data class DateTimeDifference(
        val time: Long,
        val unit: String
    ) {
        val formatted: String
            get() = "$time $unit ago"
    }

    companion object {
        private const val WORD_COUNT_BLOCK_START = "["
        private const val WORD_COUNT_BLOCK_END = "]"
        private const val UNKNOWN_AUTHOR = "Unknown"

        private const val YEAR_IN_DAYS = 365
        private const val MONTH_IN_DAYS = 30
        private const val WEEK_IN_DAYS = 7

        private const val HOUR_IN_SECONDS = 3600
        private const val MINUTE_IN_SECONDS = 60

        private val INVALID_AUTHOR_CHARACTERS = listOf("[", "]", "<", ">", "{", "}")
    }

}
