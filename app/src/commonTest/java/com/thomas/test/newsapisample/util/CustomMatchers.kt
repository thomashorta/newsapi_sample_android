package com.thomas.test.newsapisample.util

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

internal fun nonEmptyRecyclerView(): Matcher<View> = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("Checking matcher on view expecting non empty RecyclerView")
    }

    override fun matchesSafely(item: View): Boolean {
        return ((item as? RecyclerView)?.adapter?.itemCount ?: 0) > 0
    }
}

internal fun recyclerViewWithCount(count: Int): Matcher<View> = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {
        description.appendText("Checking matcher on view expecting RecyclerView with $count items")
    }

    override fun matchesSafely(item: View): Boolean {
        return ((item as? RecyclerView)?.adapter?.itemCount ?: 0) == count
    }
}

internal fun childAtPosition(
    parentMatcher: Matcher<View>,
    position: Int
): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("Child at position $position in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            val parent = view.parent
            return parent is ViewGroup && parentMatcher.matches(parent) &&
                view == parent.getChildAt(position)
        }
    }
}
