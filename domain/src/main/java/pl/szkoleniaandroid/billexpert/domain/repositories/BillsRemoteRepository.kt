package pl.szkoleniaandroid.billexpert.domain.repositories

import pl.szkoleniaandroid.billexpert.domain.model.Bill
import pl.szkoleniaandroid.billexpert.domain.model.User

interface BillsRemoteRepository {
    suspend fun login(username: String, password: String): User
    suspend fun deleteBill(objectId: String)
    suspend fun postBill(bill: Bill): Bill
    suspend fun putBill(bill: Bill): Bill
}
