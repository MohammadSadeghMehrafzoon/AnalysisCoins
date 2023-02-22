package ir.misterdeveloper.analysiscoins.repository

import ir.misterdeveloper.analysiscoins.network.RetrofitInstance
import retrofit2.http.Query

class AppRepository {

    suspend fun getTopNews() = RetrofitInstance.apiService.getTopNews("popular")
    suspend fun getTopCoins() = RetrofitInstance.apiService.getTopCoin("USD", 10)
}