
package pl.szkoleniaandroid.billexpert.features.signin

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import okhttp3.internal.wait
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.mock.declare
import pl.szkoleniaandroid.billexpert.BillExpertActivity
import pl.szkoleniaandroid.billexpert.R
import pl.szkoleniaandroid.billexpert.domain.model.User
import pl.szkoleniaandroid.billexpert.domain.repositories.BillsRemoteRepository
import pl.szkoleniaandroid.billexpert.domain.repositories.SessionRepository
import pl.szkoleniaandroid.billexpert.utils.hasTextInputLayoutErrorText

class SignInFragmentTest : KoinTest {

    @get:Rule
    val rule = ActivityTestRule<BillExpertActivity>(BillExpertActivity::class.java, true, false)

    @Test
    fun shouldShowUsernameErrorWhenEmpty() {


        //given
        val errorString = InstrumentationRegistry.getInstrumentation().targetContext
                .getString(R.string.username_cant_be_empty)

        //when
        clickLogin()

        //then
        onView(withId(R.id.username_layout))
                .check(matches(hasTextInputLayoutErrorText(errorString)))
    }

    @Test
    fun shouldShowPasswordErrorWhenEmpty() {
        //given
        val errorString = InstrumentationRegistry.getInstrumentation().targetContext
                .getString(R.string.password_cant_be_empty)

        //when
        clickLogin()

        //then
        onView(withId(R.id.password_layout))
                .check(matches(hasTextInputLayoutErrorText(errorString)))
    }

    private fun clickLogin() {
        onView(withId(R.id.login_button)).perform(click())
    }

    @Test
    fun shouldGoToBillsOnLogin() {

        val mock = mock<SessionRepository>()
        val remoteMock = mock<BillsRemoteRepository>()
        val user = User("id", "name", "", "token", false)
        runBlocking {
            whenever(mock.currentUser).thenReturn(user)
            whenever(mock.loadCurrentUser()).thenReturn(null)
            whenever(remoteMock.login("test","pass"))
                    .thenReturn(user)
        }

        declare {
            single<SessionRepository> {
                mock
            }
            single<BillsRemoteRepository> {
                remoteMock
            }
        }

        rule.launchActivity(
                Intent(
                        InstrumentationRegistry.getInstrumentation().targetContext,
                        BillExpertActivity::class.java
                )
        )

        onView(withId(R.id.username_edit_text)).perform(replaceText("test"))
        onView(withId(R.id.password_edit_text)).perform(replaceText("passx"))

        clickLogin()





    }
}