package pl.szkoleniaandroid.billexpert.domain.usecases

import pl.szkoleniaandroid.billexpert.domain.model.Bill
import pl.szkoleniaandroid.billexpert.domain.repositories.BillRepository
import pl.szkoleniaandroid.billexpert.domain.repositories.BillsRemoteRepository


class UpdateBillUseCase(
        private val billsRepository: BillsRemoteRepository,
        private val billRepository: BillRepository
) {
    suspend operator fun invoke(bill: Bill) {
        val updatedBill = billsRepository.putBill(bill)
        billRepository.updateBill(updatedBill)
    }
}
