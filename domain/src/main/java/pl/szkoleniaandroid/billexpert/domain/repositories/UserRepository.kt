package pl.szkoleniaandroid.billexpert.domain.repositories

import pl.szkoleniaandroid.billexpert.domain.model.User

interface UserRepository {

    suspend fun save(user: User)

    fun getUserByAuthData(username: String, password: String): User?

    fun getUserById(userId: String): User
}
