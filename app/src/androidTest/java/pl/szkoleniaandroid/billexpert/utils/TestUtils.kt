package pl.szkoleniaandroid.billexpert.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.facebook.testing.screenshot.Screenshot
import org.hamcrest.CoreMatchers
import pl.szkoleniaandroid.billexpert.BillExpertActivity
import pl.szkoleniaandroid.billexpert.R
import java.util.concurrent.TimeUnit

/**
 * Used for testing fragments inside a fake activity.
 */
class ActivityForTestingFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_test_activity)
    }

    fun initWithNavigation(
            @IdRes destinationId: Int,
            @NavigationRes navigationId: Int
    ) {
        with(findNavController(R.id.test_nav_host_fragment)) {
            val graphToSet = navInflater.inflate(navigationId)
            graphToSet.startDestination = destinationId
            setGraph(graphToSet, intent.extras)
        }
    }
}

fun fragmentTestRuleWithMocks(stubbing: () -> Unit = {}) =
        object : ActivityTestRule<ActivityForTestingFragment>(
                ActivityForTestingFragment::class.java, true, false
        ) {
            override fun beforeActivityLaunched() {
                super.beforeActivityLaunched()
                stubbing()
            }
        }

abstract class Robot<T : Activity>(protected val rule: ActivityTestRule<T>) {

    fun startMainActivity() {
        rule.launchActivity(
                Intent(
                        InstrumentationRegistry.getInstrumentation().targetContext,
                        BillExpertActivity::class.java
                )
        )
    }

    fun wait(seconds: Int) = TimeUnit.SECONDS.sleep(seconds.toLong())

    fun waitMs(milliseconds: Int) = TimeUnit.MILLISECONDS.sleep(milliseconds.toLong())

    fun checkWasToastShown(text: String) {
        Espresso.onView(ViewMatchers.withText(text))
                .inRoot(RootMatchers.withDecorView(CoreMatchers.not(rule.activity.window.decorView)))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    fun hideKeyboard() {
        Espresso.closeSoftKeyboard()
    }
}

abstract class FragmentRobot(rule: ActivityTestRule<ActivityForTestingFragment>) :
        Robot<ActivityForTestingFragment>(rule) {

    fun startFragmentWithNav(
            @IdRes destinationId: Int,
            @NavigationRes navigationId: Int = R.navigation.nav_bill_expert
    ) {
        rule.launchActivity(
                Intent(
                        InstrumentationRegistry.getInstrumentation().targetContext,
                        ActivityForTestingFragment::class.java
                )
        )
        rule.activity.initWithNavigation(destinationId, navigationId)
    }

    fun capture(tag: String, description: String = "") {
        wait(1)
        Screenshot.snapActivity(rule.activity)
                .setName(tag)
                .setDescription(description)
                .record()
    }

    fun pressBack() = Espresso.pressBackUnconditionally()
}


