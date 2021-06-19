package pl.szkoleniaandroid.billexpert.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import pl.szkoleniaandroid.billexpert.domain.model.Bill
import pl.szkoleniaandroid.billexpert.domain.repositories.BillRepository

class BillRoomRepository(private val billDao: BillDao) : BillRepository {
    override suspend fun getMaxUpdatedAt(userId: String): LocalDateTime {
        return GlobalScope.async {
            billDao.getMaxUpdatedAt(userId)?.value ?: LocalDateTime.MIN
        }.await()
    }

    override suspend fun getMinUpdatedAt(userId: String): LocalDateTime {
        return GlobalScope.async {
            billDao.getMinUpdatedAt(userId)?.value ?: LocalDateTime.MAX
        }.await()
    }

    override suspend fun updateBill(bill: Bill) {
        GlobalScope.async {
            billDao.update(bill.toDto())
        }.await()
    }

    override fun getTotalAmount(userId: String) =
            billDao.getTotalAmountForUser(userId).map { it.value }

    override suspend fun saveBill(bill: Bill) {
        GlobalScope.async {
            billDao.insert(bill.toDto())
        }.await()
    }

    override suspend fun getBillsCount(userId: String): Int {
        return billDao.getBillsCount(userId)
    }

    override suspend fun saveBills(bills: List<Bill>) {
        GlobalScope.async {
            billDao.insert(bills.map { it.toDto() }.toList())
        }.await()
    }

    override suspend fun deleteBill(bill: Bill) {
        GlobalScope.async {
            billDao.deleteBill(bill.toDto())
        }.await()
    }
}

fun BillDto.toBill(): Bill = Bill(
        userId = userId,
        date = date,
        name = name,
        amount = amount,
        category = category,
        comment = comment,
        objectId = objectId,
        createdAt = createdAt,
        updatedAt = updatedAt
)

fun Bill.toDto(): BillDto = BillDto().let {
    it.objectId = objectId
    it.userId = userId
    it.date = date
    it.name = name
    it.amount = amount
    it.category = category
    it.comment = comment
    it.createdAt = createdAt ?: LocalDateTime.MIN
    it.updatedAt = updatedAt ?: LocalDateTime.MIN
    return it
}
