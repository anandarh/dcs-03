package com.anandarh.storyapp.ui.activities

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.anandarh.storyapp.R
import com.anandarh.storyapp.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class ListStoryActivityTest {
    @get:Rule
    val activity = ActivityScenarioRule(ListStoryActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadStoryList_Success() {
        onView(withId(R.id.rv_stories)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_stories)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
    }

    @Test
    fun loadStoryDetail_Success() {
        Intents.init()
        onView(withId(R.id.rv_stories)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        intended(hasComponent(DetailStoryActivity::class.java.name))
        onView(withId(R.id.iv_detail_photo)).check(matches(isDisplayed()))
    }
}