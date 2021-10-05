package com.thomas.test.newsapisample.example

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import com.google.common.truth.Truth.assertThat
import com.thomas.test.newsapisample.MainActivity
import com.thomas.test.newsapisample.feature.sourcelist.SourceListFragment
import com.thomas.test.newsapisample.robot.MainActivityRobot
import com.thomas.test.newsapisample.util.nonEmptyRecyclerView
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

class MainActivityE2ECoupledTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun test_sourcesLoaded() {
        Thread.sleep(3000)

        scenario.onActivity {
            val sourceFragment = it.supportFragmentManager
                .findFragmentByTag(MainActivity.TAG_SOURCE_LIST)

            (sourceFragment as SourceListFragment).apply {
                assertThat(rvSourcesRef.isVisible).isTrue()
                assertThat(rvSourcesRef.adapter!!.itemCount).isGreaterThan(0)
            }
        }
    }
}
















class MainActivityE2EEspressoTest {
    private lateinit var scenario: ActivityScenario<MainActivity>

    @Before
    fun setUp() {
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun test_sourcesLoaded() {
        Thread.sleep(3000)

        val recyclerMatcher = withClassName(`is`(RecyclerView::class.qualifiedName))

        onView(
            recyclerMatcher
        ).check(
            matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )

        onView(
            recyclerMatcher
        ).check(
            matches(
                nonEmptyRecyclerView()
            )
        )
    }
}



















class MainActivityE2ERobotTest {
    private lateinit var robot: MainActivityRobot

    @Before
    fun setUp() {
        robot = MainActivityRobot()
    }

    @After
    fun tearDown() {
        robot.tearDown()
    }

    @Test
    fun test_sourcesLoaded() {
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
            }
        }
    }
}
