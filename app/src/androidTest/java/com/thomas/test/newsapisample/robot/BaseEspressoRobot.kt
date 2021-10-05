package com.thomas.test.newsapisample.robot

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.thomas.test.newsapisample.util.nonEmptyRecyclerView
import com.thomas.test.newsapisample.util.recyclerViewWithCount

abstract class BaseEspressoRobot {
    fun sleep(millis: Long) = Thread.sleep(millis)

    protected fun viewIsVisible(id: Int) {
        Espresso.onView(
            ViewMatchers.withId(id)
        ).check(
            ViewAssertions.matches(
                ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        )
    }

    protected fun recyclerViewIsNotEmpty(id: Int) {
        Espresso.onView(
            ViewMatchers.withId(id)
        ).check(
            ViewAssertions.matches(
                nonEmptyRecyclerView()
            )
        )
    }

    protected fun recyclerViewHasCount(id: Int, count: Int) {
        Espresso.onView(
            ViewMatchers.withId(id)
        ).check(
            ViewAssertions.matches(
                recyclerViewWithCount(count)
            )
        )
    }

    protected fun clickRecyclerViewItem(recyclerViewId: Int, itemPosition: Int) {
        Espresso.onView(
            ViewMatchers.withId(recyclerViewId)
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                itemPosition,
                ViewActions.click()
            )
        )
    }
}
