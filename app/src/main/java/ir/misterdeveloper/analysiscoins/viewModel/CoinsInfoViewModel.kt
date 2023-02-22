package ir.misterdeveloper.analysiscoins.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ir.misterdeveloper.analysiscoins.R
import ir.misterdeveloper.analysiscoins.app.MyApplication
import ir.misterdeveloper.analysiscoins.model.CoinsData
import ir.misterdeveloper.analysiscoins.model.CoinsResponse
import ir.misterdeveloper.analysiscoins.repository.AppRepository
import ir.misterdeveloper.analysiscoins.util.Resource
import ir.misterdeveloper.analysiscoins.util.Utils
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CoinsInfoViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {


    val coinsInfo: MutableLiveData<Resource<CoinsResponse>> = MutableLiveData()

    init {
        getTopCoins()
    }

    private fun getTopCoins() = viewModelScope.launch {
        fetchCoins()
    }

    private suspend fun fetchCoins() {
        coinsInfo.postValue(Resource.Loading())
        try {
            if (Utils.hasInternetConnection(getApplication<MyApplication>())) {
                val response = appRepository.getTopCoins()
                response.body()
                coinsInfo.postValue(handleCoinsResponse(response))
            } else {
                coinsInfo.postValue(Resource.Error(getApplication<MyApplication>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> coinsInfo.postValue(
                    Resource.Error(
                        getApplication<MyApplication>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> coinsInfo.postValue(
                    Resource.Error(
                        getApplication<MyApplication>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleCoinsResponse(response: Response<CoinsResponse>): Resource<CoinsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}