package com.thomas.test.newsapisample

import com.thomas.test.newsapisample.robot.MainActivityRobot
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainActivityE2ETest {
    lateinit var robot: MainActivityRobot

    @Before
    fun setUp() {
        robot = MainActivityRobot()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun test_navigateFromSourceListToArticleContent() {
        with(robot) {
            arrange {
                launch()
            }

            sleep(3000)

            onSourceList {
                assert {
                    sourceListIsVisible()
                    sourceListIsNotEmpty()
                }

                act {
                    clickSourceListItem(0)
                }
            }

            sleep(3000)

            onArticleList {
                assert {
                    articleListIsVisible()
                    articleListIsNotEmpty()
                }

                act {
                    clickArticleListItem(0)
                }
            }

            sleep(3000)

            onArticleContent {
                assert {
                    contentTitleIsVisible()
                    contentAuthorIsVisible()
                    contentTextIsVisible()
                    contentImageIsVisible()
                }
            }
        }
    }
}
