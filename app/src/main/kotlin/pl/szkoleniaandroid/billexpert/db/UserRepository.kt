package pl.szkoleniaandroid.billexpert.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.szkoleniaandroid.billexpert.domain.model.User
import pl.szkoleniaandroid.billexpert.domain.repositories.UserRepository
import pl.szkoleniaandroid.billexpert.security.hash

class RoomUserRepository(private val userDao: UserDao) : UserRepository {

    override suspend fun save(user: User) = withContext(Dispatchers.IO) {
        userDao.insert(
                UserDto().apply {
                    this.objectId = user.objectId
                    this.username = user.username
                    this.password = user.password
                    this.token = user.token
                    this.isAdmin = user.isAdmin
                }
        )
    }

    override fun getUserByAuthData(username: String, password: String): User? {
        //keeping passwords LOCALLY in MD5, WITHOUT SALT is as #INSECURE as it can get
        val md5Password = password.hash()
        val userDto = userDao.getUserByCredentials(username, md5Password)
                ?: return null
        return User(
                objectId = userDto.objectId,
                username = userDto.username,
                password = userDto.password,
                token = userDto.token,
                isAdmin = userDto.isAdmin
        )
    }

    override fun getUserById(userId: String): User {
        val userDto = userDao.getUserById(userId)
        return User(
                objectId = userDto.objectId,
                username = userDto.username,
                password = userDto.password,
                token = userDto.token,
                isAdmin = userDto.isAdmin
        )
    }
}

