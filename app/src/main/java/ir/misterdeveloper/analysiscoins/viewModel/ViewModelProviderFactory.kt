package ir.misterdeveloper.analysiscoins.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.misterdeveloper.analysiscoins.repository.AppRepository

class ViewModelProviderFactory(
    val app: Application,
    val appRepository: AppRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(app, appRepository) as T
        }

        if (modelClass.isAssignableFrom(CoinsInfoViewModel::class.java)) {
            return CoinsInfoViewModel(app, appRepository) as T
        }



        throw IllegalArgumentException("Unknown class name")
    }



}