package pl.szkoleniaandroid.billexpert.api

import org.threeten.bp.LocalDateTime
import pl.szkoleniaandroid.billexpert.domain.model.Bill

class LoginResponse(
        val username: String,
        val objectId: String,
        val sessionToken: String,
        val isAdmin: Boolean
)

class BillsResponse(
        val results: List<Bill>
)

class PutBillResponse(
        val updatedAt: LocalDateTime
)

class PostBillResponse(
        val objectId: String,
        val createdAt: LocalDateTime
)
