package pl.szkoleniaandroid.billexpert.domain.model

data class User(
        val objectId: String,
        val username: String,
        val password: String,
        val token: String,
        val isAdmin: Boolean
)
