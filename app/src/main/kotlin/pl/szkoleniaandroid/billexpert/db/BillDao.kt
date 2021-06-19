package pl.szkoleniaandroid.billexpert.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime

@Dao
@TypeConverters(Converters::class)
interface BillDao {

    @Insert
    fun insert(billDto: BillDto)

    @Query("SELECT * from bill where userId = :userId ORDER BY updatedAt DESC")
    fun getAllForUser(userId: String): DataSource.Factory<Int, BillDto>

    @Query("SELECT sum(amount) as value from bill where userId = :userId")
    fun getTotalAmountForUser(userId: String): Flow<Amount>


    @Query("SELECT max(updatedAt) as value from bill where userId = :userId")
    fun getMaxUpdatedAt(userId: String): DateTimeWrapper?

    @Query("SELECT min(updatedAt) as value from bill where userId = :userId")
    fun getMinUpdatedAt(userId: String): DateTimeWrapper?


    @Update
    fun update(billDto: BillDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(billDto: List<BillDto>)

    @Delete
    fun deleteBill(billDto: BillDto)

    @Query("SELECT count(1) as value from bill where userId = :userId")
    suspend fun getBillsCount(userId: String): Int
}

//wrapper class for sum in sql
class Amount(var value: Double)

class DateTimeWrapper(val value: LocalDateTime)
