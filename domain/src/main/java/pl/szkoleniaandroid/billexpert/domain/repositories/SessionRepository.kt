package pl.szkoleniaandroid.billexpert.domain.repositories

import pl.szkoleniaandroid.billexpert.domain.model.User

interface SessionRepository {
    val currentUser: User?

    fun saveCurrentUser(user: User)
    fun clearCurrentUser()
    suspend fun loadCurrentUser(): User?
}
