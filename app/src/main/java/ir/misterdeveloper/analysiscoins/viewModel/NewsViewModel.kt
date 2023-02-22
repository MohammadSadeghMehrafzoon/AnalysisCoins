package ir.misterdeveloper.analysiscoins.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ir.misterdeveloper.analysiscoins.R
import ir.misterdeveloper.analysiscoins.app.MyApplication
import ir.misterdeveloper.analysiscoins.model.NewsData
import ir.misterdeveloper.analysiscoins.repository.AppRepository
import ir.misterdeveloper.analysiscoins.util.Resource
import ir.misterdeveloper.analysiscoins.util.Utils.hasInternetConnection
import kotlinx.coroutines.launch


import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    app: Application,
    private val appRepository: AppRepository
) : AndroidViewModel(app) {


    val newsData: MutableLiveData<Resource<NewsData>> = MutableLiveData()

    init {
        getTopNews()
    }

    private fun getTopNews() = viewModelScope.launch {
        fetchNews()
    }

    private suspend fun fetchNews() {
        newsData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection(getApplication<MyApplication>())) {
                val response = appRepository.getTopNews()
                newsData.postValue(handleNewsResponse(response))
            } else {
                newsData.postValue(Resource.Error(getApplication<MyApplication>().getString(R.string.no_internet_connection)))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> newsData.postValue(
                    Resource.Error(
                        getApplication<MyApplication>().getString(
                            R.string.network_failure
                        )
                    )
                )
                else -> newsData.postValue(
                    Resource.Error(
                        getApplication<MyApplication>().getString(
                            R.string.conversion_error
                        )
                    )
                )
            }
        }
    }

    private fun handleNewsResponse(response: Response<NewsData>): Resource<NewsData> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


}