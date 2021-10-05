package com.thomas.test.newsapisample.feature.articlelist

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.robot.ArticleListFragmentRobot
import com.thomas.test.newsapisample.util.ArticleFactory
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleListFragmentUnitTest {
    @MockK(relaxUnitFun = true)
    lateinit var mockCallback: BaseFragment.ActivityCallback

    private lateinit var robot: ArticleListFragmentRobot

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        robot = ArticleListFragmentRobot()
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test_successArticlesLoad() {
        with(robot) {
            arrange {
                mockViewModelSuccess(FAKE_ARTICLE_LIST)
                launch(mockCallback)
            }

            assert {
                articleListIsVisible()
                articleListHasCount(3)
            }
        }
    }

    @Test
    fun test_failureArticlesLoad() {
        with(robot) {
            arrange {
                mockViewModelFailure()
                launch(mockCallback)
            }

            assert {
                articleListIsNotVisible()
                errorIsVisible()
            }
        }
    }

    @Test
    fun test_loadingArticlesLoad() {
        with(robot) {
            arrange {
                mockViewModelLoading()
                launch(mockCallback)
            }

            assert {
                articleListIsNotVisible()
                loadingIsVisible()
            }
        }
    }

    private val FAKE_ARTICLE_LIST = listOf(
        ArticleFactory.createArticle("one"),
        ArticleFactory.createArticle("two"),
        ArticleFactory.createArticle("three"),
    )
}
