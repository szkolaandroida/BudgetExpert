package pl.szkoleniaandroid.billexpert.features.signin

import android.app.Activity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.mock.declare
import pl.szkoleniaandroid.billexpert.R
import pl.szkoleniaandroid.billexpert.utils.*
import okhttp3.mockwebserver.MockWebServer


class SignInFragmentShould : FragmentTest<SignInRobot>() {

    @get:Rule
    val rule = fragmentTestRuleWithMocks {
        declare {
            single { "http://localhost:9999" }
        }
    }

    override fun createRobot() = SignInRobot(rule)


    @Test
    fun showErrorOnEmptyUsername() {

        withRobot {
            startFragmentWithNav(R.id.nav_sign_in)
            clickSignIn()
            checkUsernameError("Username can't be empty!")
            capture("sign_in_username_error")
        }
    }

    @Test
    fun showErrorFromApiOnWrongCredentials() {
        withRobot {

            val server = MockWebServer()
            server.enqueue(MockResponse().setBody("{\"code\":101,\"error\":\"Invalid username/password.\"}").setResponseCode(404))
            server.start(9999)

            startFragmentWithNav(R.id.nav_sign_in)
            enterUsername("test")
            enterPassword("bad")
            clickSignIn()
            checkWasToastShown("Invalid username/password.")
            capture("sign_in_api_error")
        }
    }


}

class SignInRobot(rule: ActivityTestRule<ActivityForTestingFragment>) : FragmentRobot(rule) {

    fun clickSignIn() {
        R.id.login_button.click()
    }

    fun checkUsernameError(error: String) {
        R.id.username_layout.withError(error)
    }

    fun enterUsername(username: String) {
        onView(withId(R.id.username_edit_text)).perform(replaceText(username))
    }

    fun enterPassword(password: String) {
        onView(withId(R.id.password_edit_text)).perform(replaceText(password))
    }

}

abstract class RobotTest<A : Activity, R : Robot<A>> : KoinTest {

    protected fun withRobot(steps: R.() -> Unit) {
        createRobot().apply(steps)
    }

    abstract fun createRobot(): R
}

abstract class FragmentTest<R : FragmentRobot> : RobotTest<ActivityForTestingFragment, R>()

fun Int.click() {
    onView(withId(this)).perform(ViewActions.click())
}

fun Int.withError(error: String) {
    onView(withId(this)).check(matches(hasTextInputLayoutErrorText(error)))
}