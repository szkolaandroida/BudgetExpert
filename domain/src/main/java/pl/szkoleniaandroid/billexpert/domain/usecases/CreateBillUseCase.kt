package pl.szkoleniaandroid.billexpert.domain.usecases

import pl.szkoleniaandroid.billexpert.domain.model.Bill
import pl.szkoleniaandroid.billexpert.domain.repositories.BillRepository
import pl.szkoleniaandroid.billexpert.domain.repositories.BillsRemoteRepository

class CreateBillUseCase(
        private val billsRepository: BillsRemoteRepository,
        private val billRepository: BillRepository
) {
    suspend operator fun invoke(bill: Bill) {
        val body = billsRepository.postBill(bill)
        val newBill = bill.copy(objectId = body.objectId, createdAt = body.createdAt,
                updatedAt = body.createdAt)
        billRepository.saveBill(newBill)

    }
}
