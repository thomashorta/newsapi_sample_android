package com.thomas.test.newsapisample.feature.sourcelist

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.thomas.test.newsapisample.feature.common.BaseFragment
import com.thomas.test.newsapisample.robot.SourceListFragmentRobot
import com.thomas.test.newsapisample.util.SourceFactory
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SourceListFragmentIntegrationTest {
    @MockK(relaxUnitFun = true)
    lateinit var mockCallback: BaseFragment.ActivityCallback

    private lateinit var robot: SourceListFragmentRobot

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        robot = SourceListFragmentRobot()
    }

    @After
    fun tearDown() {
        robot.tearDown()
        unmockkAll()
    }

    @Test
    fun test_sourcesLoaded() {
        with(robot) {
            arrange {
                mockSuccessRepository(FAKE_SOURCE_LIST)
                launch(mockCallback)
            }

            assert {
                sourceListIsVisible()
                sourceListHasCount(2)
            }
        }
    }

    @Test
    fun test_sourceClicked() {
        with(robot) {
            arrange {
                mockSuccessRepository(FAKE_SOURCE_LIST)
                launch(mockCallback)
            }

            sleep(3000)

            act {
                clickSourceListItem(0)
            }

            verify(exactly = 1) {
                mockCallback.showArticles(FAKE_SOURCE_LIST[0])
            }
        }
    }

    private val FAKE_SOURCE_LIST = listOf(
        SourceFactory.createSource("one"),
        SourceFactory.createSource("two")
    )
}
