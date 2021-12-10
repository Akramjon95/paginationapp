package maxcoders.uz.aliftechapp.network

import maxcoders.uz.aliftechapp.model.Data
import maxcoders.uz.aliftechapp.model.DataResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("upcomingGuides/")
    suspend fun getData(): Response<DataResponse>

    @GET("upcomingGuides/")
    suspend fun getPagingData(
        @Query("page")page: Int,
        @Query("limit")limit: Int
    ): Response<DataResponse>
}