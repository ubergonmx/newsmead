package com.newsmead

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.idling.CountingIdlingResource
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.newsmead.activities.SplashActivity
import org.junit.Rule
import org.junit.Test

class SplashScreenTest {

    // Our app starts at the splash screen.
    @get:Rule
    val activityRule = ActivityScenarioRule(SplashActivity::class.java)

    // Test this before starting any other tests
    @Test
    fun moveToAccountActivity() {
        val idlingResource = CountingIdlingResource("SplashScreen")

        Espresso.onView(ViewMatchers.withId(R.id.ivLogo))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Register the idling resource before the action that triggers the wait
        IdlingRegistry.getInstance().register(idlingResource)

        // Perform actions that will cause a delay (e.g., waiting for 5 seconds)
        // Here, we increment the counting idling resource to signal that we are waiting
        idlingResource.increment()

        // Wait for 3 seconds (3000 milliseconds)
        Thread.sleep(3000)

        // Now decrement the counting idling resource to signal that the wait is over
        idlingResource.decrement()

        // Unregister the idling resource when done
        IdlingRegistry.getInstance().unregister(idlingResource)

        // Check that the current activity is no longer the splash screen
        Espresso.onView(ViewMatchers.withId(R.id.ivLogo))
            .check(ViewAssertions.doesNotExist())
    }
}