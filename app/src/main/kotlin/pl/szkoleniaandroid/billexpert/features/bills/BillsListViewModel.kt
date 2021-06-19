package pl.szkoleniaandroid.billexpert.features.bills

import androidx.lifecycle.*
import androidx.paging.*
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.tatarka.bindingcollectionadapter2.OnItemBind
import org.json.JSONObject
import pl.szkoleniaandroid.billexpert.BR
import pl.szkoleniaandroid.billexpert.R
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.db.BillDao
import pl.szkoleniaandroid.billexpert.db.BillDto
import pl.szkoleniaandroid.billexpert.db.toBill
import pl.szkoleniaandroid.billexpert.domain.repositories.BillRepository
import pl.szkoleniaandroid.billexpert.domain.repositories.SessionRepository
import timber.log.Timber
import java.net.UnknownHostException

class BillsListViewModel(
        private val billApi: BillApi,
        private val sessionRepository: SessionRepository,
        private val billRepository: BillRepository,
        private val billDao: BillDao
) : ViewModel() {

    val shouldShowAdmin: Boolean = sessionRepository.currentUser!!.isAdmin

    val totalAmount: LiveData<Double> =
            billRepository.getTotalAmount(sessionRepository.currentUser!!.objectId).asLiveData()

    val factory: DataSource.Factory<Int, BillItem> = billDao.getAllForUser(sessionRepository.currentUser!!.objectId)
            .map { it.toBillItem() }
    val pagedList: LiveData<PagedList<BillItem>> = factory.toLiveData(3, boundaryCallback = object :PagedList.BoundaryCallback<BillItem>() {
        override fun onZeroItemsLoaded() {
            super.onZeroItemsLoaded()
            loadBills(true)
        }

        override fun onItemAtEndLoaded(itemAtEnd: BillItem) {
            super.onItemAtEndLoaded(itemAtEnd)
            loadBills(true)
        }
    });

    val diff = object : DiffUtil.ItemCallback<BillItem>() {
        override fun areItemsTheSame(oldItem: BillItem, newItem: BillItem): Boolean {
            return oldItem == newItem //id
        }

        override fun areContentsTheSame(oldItem: BillItem, newItem: BillItem): Boolean {
            return oldItem == newItem
        }

    }


    val isLoadingLiveData = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(pagedList) { bills ->
            setValue(bills.isEmpty())
        }
        addSource(isLoadingLiveData) { loading ->
            if (loading) {
                value = false
            }
        }
    }

    val itemBinding: OnItemBind<BillItem> = OnItemBind { itemBinding, _, item ->
        itemBinding.set(BR.item, R.layout.bill_item)
        itemBinding.bindExtra(BR.listener, object : OnBillClickedListener {
            override fun onBillClicked(bill: BillItem) {
                view?.editBill(bill.bill)
            }
        })
    }

    var view: BillsView? = null

    fun loadBills(force: Boolean) {

        isLoadingLiveData.value = true
        GlobalScope.launch(Dispatchers.Main) {

            sessionRepository.currentUser?.objectId?.let { userId ->

                val maxUpdatedAt = billRepository.getMaxUpdatedAt(userId)
                val where = JSONObject()
                where.put("userId", userId)
//                val updatedAtWhere = JSONObject()
//                val isoDate = LOCAL_DATE_TIME_FORMATTER.format(maxUpdatedAt)
//                val jsonDate = JSONObject()
//                jsonDate.put("__type", "Date")
//                jsonDate.put("iso", isoDate)
//
//                updatedAtWhere.put(if (force) "\$gte" else "\$eq", jsonDate)
//                if (force) {
//                    where.put("updatedAt", updatedAtWhere)
//                } else {
//                    where.put("date", )
//                }
//
                val skip = billRepository.getBillsCount(userId)
                try {

                    val bills = billApi.getBillsForUser(where.toString(), limit = 3, order = "-date", skip = skip).results
                    GlobalScope.launch {
                        billRepository.saveBills(bills)
                        withContext(Dispatchers.Main) {
                            isLoadingLiveData.value = false
                        }
                    }
                } catch (e: UnknownHostException) {
                    Timber.e(e)
                    //TODO handle errors
                }
            }


        }

    }

    fun logout() {
        sessionRepository.clearCurrentUser()
    }

    fun getCsvBody(): String {
        val sb = StringBuilder()
        sb.append("NAME,CATEGORY,AMOUNT,DATE\n")
//        pagedList.value?.forEach { bill ->
//            sb.append(bill.name)
//            sb.append(',')
//            sb.append(bill.bill.category)
//            sb.append(',')
//            sb.append(bill.amount)
//            sb.append(',')
//            sb.append(bill.bill.date)
//            sb.append('\n')
//        }
        return sb.toString()
    }
}

fun BillDto.toBillItem(): BillItem {
    return BillItem(
            name = this.name,
            comment = this.comment,
            amount = this.amount,
            categoryUrl = "file:///android_asset/${this.category.name.toLowerCase()}.png",
            bill = this.toBill()
    )
}
