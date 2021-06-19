package pl.szkoleniaandroid.billexpert.domain.usecases

import pl.szkoleniaandroid.billexpert.domain.repositories.BillsRemoteRepository
import pl.szkoleniaandroid.billexpert.domain.repositories.SessionRepository
import pl.szkoleniaandroid.billexpert.domain.repositories.UserRepository


class SignInUseCase(
        private val billsRepository: BillsRemoteRepository,
        private val sessionRepository: SessionRepository,
        private val userRepository: UserRepository
) {

    suspend operator fun invoke(username: String, password: String): Unit {
        val user = billsRepository.login(username, password)
        userRepository.save(user)
        sessionRepository.saveCurrentUser(user)
    }
}

