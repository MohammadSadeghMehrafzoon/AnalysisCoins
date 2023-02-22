package ir.misterdeveloper.analysiscoins.network

import ir.misterdeveloper.analysiscoins.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("v2/news/")
    suspend fun getTopNews(@Query("sortOrder") sortOrder: String = "popular"): Response<NewsData>

    @GET("top/totalvolfull")
    suspend fun getTopCoin(
        @Query("tsym") to_symbol: String = "USD",
        @Query("limit") limitData: Int = 20
    ): Response<CoinsResponse>


}