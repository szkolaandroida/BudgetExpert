package pl.szkoleniaandroid.billexpert.api

import kotlinx.coroutines.Deferred
import pl.szkoleniaandroid.billexpert.domain.model.Bill
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

const val REST_APP_ID = "RRQfzogXeuQI2VzK0bqEgn02IElfm3ifCUf1lNQX"
const val REST_API_KEY = "mt4btJUcnmVaEJGzncHqkogm0lDM3n2185UNSjiX"
const val BASE_URL = "https://parseapi.back4app.com/"

interface BillApi {

    @Headers(
            "X-Parse-Revocable-Session: 1"
    )
    @GET("login")
    suspend fun getLogin(@Query("username") username: String,
                         @Query("password") password: String): LoginResponse


    @POST("classes/Bill")
    suspend fun postBill(@Body bill: Bill): PostBillResponse

    @GET("classes/Bill")
    suspend fun getBillsForUser(@Query("where") where: String, @Query("limit") limit: Int, @Query("skip") skip: Int, @Query("order") order: String): BillsResponse

    @PUT("classes/Bill/{id}")
    suspend fun putBill(@Body bill: Bill, @Path("id") objectId: String): PutBillResponse

    @DELETE("classes/Bill/{id}")
    suspend fun deleteBill(@Path("id") objectId: String)
}
