package pl.szkoleniaandroid.billexpert.features.signin

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import pl.szkoleniaandroid.billexpert.R
import pl.szkoleniaandroid.billexpert.domain.model.User
import pl.szkoleniaandroid.billexpert.domain.repositories.BillsRemoteRepository
import pl.szkoleniaandroid.billexpert.domain.usecases.SignInUseCase
import pl.szkoleniaandroid.billexpert.utils.StringProvider

class LoginViewModelTest {

    @Test
    fun shouldGoToBillsWhenLoginCorrect() {

        //given
        val error = "Username can't be empty"
        val stringProvider = mock<StringProvider>()

        whenever(stringProvider.getString(R.string.username_cant_be_empty)).thenReturn(error)
        whenever(stringProvider.getString(R.string.password_cant_be_empty)).thenReturn("Password error")

        val signInUseCase = SignInUseCase(mock(), mock(), mock())
        val viewModel = LoginViewModel(stringProvider, signInUseCase)

        //when
        viewModel.loginClicked()

        //then

        assertEquals(error, viewModel.usernameError.get())
        assertEquals("Password error", viewModel.passwordError.get())

        val captor = ArgumentCaptor.forClass(Int::class.java)
        verify(stringProvider, times(2)).getString(captor.capture())
        assertEquals(R.string.username_cant_be_empty, captor.allValues[0])

    }


    @Test
    fun shouldGoToBills() {
        val stringProvider = mock<StringProvider>()

        val billsRepository = mock<BillsRemoteRepository>()
        val signInUseCase = SignInUseCase(billsRepository, mock(), mock())
        val viewModel = LoginViewModel(stringProvider, signInUseCase)
        runBlocking {


            whenever(billsRepository.login(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
                    .thenReturn(User("","","","", false))

        }
        //when
        viewModel.username.set("user")
        viewModel.password.set("password")
        viewModel.loginClicked()

        //then
        assertEquals(LoginSuccessful, viewModel.uiState.value)


    }

}