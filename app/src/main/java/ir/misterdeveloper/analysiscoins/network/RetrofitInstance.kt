package ir.misterdeveloper.analysiscoins.network


import ir.misterdeveloper.analysiscoins.util.API_KEY
import ir.misterdeveloper.analysiscoins.util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {


    companion object {


        private val retrofit by lazy {
            val client = OkHttpClient.Builder().addInterceptor { chain ->
                val newRequest: Request = chain.request().newBuilder()
                    .addHeader("api_key", API_KEY)
                    .build()
                chain.proceed(newRequest)
            }.followRedirects(false)
                .followSslRedirects(false)
                .build()


            Retrofit.Builder().baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }


        val apiService by lazy {
            retrofit.create(ApiService::class.java)
        }

    }
}