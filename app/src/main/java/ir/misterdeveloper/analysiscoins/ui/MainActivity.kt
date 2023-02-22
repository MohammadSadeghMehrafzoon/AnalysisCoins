package ir.misterdeveloper.analysiscoins.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import ir.misterdeveloper.analysiscoins.databinding.ActivityMainBinding
import ir.misterdeveloper.analysiscoins.repository.AppRepository
import ir.misterdeveloper.analysiscoins.util.Resource
import ir.misterdeveloper.analysiscoins.viewModel.NewsViewModel
import ir.misterdeveloper.analysiscoins.viewModel.ViewModelProviderFactory
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.snackbar.Snackbar
import ir.misterdeveloper.analysiscoins.adapter.CoinsAdapter
import ir.misterdeveloper.analysiscoins.model.CoinsData
import ir.misterdeveloper.analysiscoins.util.errorSnack
import ir.misterdeveloper.analysiscoins.viewModel.CoinsInfoViewModel


class MainActivity : AppCompatActivity(),CoinsAdapter.RecyclerCallback {

    lateinit var binding: ActivityMainBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var coinsViewModel: CoinsInfoViewModel
    private val newsList: ArrayList<Pair<String, String>> = arrayListOf()
    private val shimmerFrameLayout: ShimmerFrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupViewModel()
        initUi()


        binding.swipeRefresh.setOnRefreshListener {

            initUi()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipeRefresh.isRefreshing = false
            }, 3000)

        }

        binding.ItemNews.buttonShowMore.setOnClickListener {

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)

        }

    }


    private fun initUi() {
        getTopNews()
        getTopCoins()
    }

    private fun setupViewModel() {
        val repository = AppRepository()
        val factory = ViewModelProviderFactory(application, repository)
        newsViewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
        coinsViewModel = ViewModelProvider(this, factory)[CoinsInfoViewModel::class.java]

    }

    private fun getTopNews() {

        visibleShimmer()

        newsViewModel.newsData.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {

                    goneShimmer()

                    response.data?.let { newsResponse ->



                        val data = response.data

                        data.data.forEach {
                            newsList.add(Pair(it.title, it.url))
                        }

                        loadNews()

                    }
                }

                is Resource.Error -> {

                    response.message?.let { message ->
                        binding.coordinator.errorSnack(message, Snackbar.LENGTH_LONG)
                        Log.v("news", message)
                    }

                    binding.swipeRefresh.isRefreshing = false

                }

                is Resource.Loading -> {

                    visibleShimmer()


                }
            }
        })

    }

    private fun loadNews() {

        val randomNews = (0..newsList.size).random()

        binding.layoutNews.textViewNews.text = newsList[randomNews].first
        binding.layoutNews.textViewNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(newsList[randomNews].second))
            startActivity(intent)
        }

    }

    private fun getTopCoins() {

        visibleShimmer()

        coinsViewModel.coinsInfo.observe(this, Observer { coinsResponse ->
            when (coinsResponse) {
                is Resource.Success -> {

                    goneShimmer()

                    coinsResponse.data?.let { coinsResponse ->

                        Log.v("coins", coinsResponse.toString())

                        binding.ItemNews.recyclerItemCoins.adapter =
                            CoinsAdapter(
                                applicationContext,
                                coinsResponse = coinsResponse.data,
                                this
                            )
                        binding.ItemNews.recyclerItemCoins.layoutManager = LinearLayoutManager(
                            applicationContext,
                            RecyclerView.VERTICAL,
                            false
                        )


                    }
                }

                is Resource.Error -> {

                    coinsResponse.message?.let { message ->
                        binding.coordinator.errorSnack(message, Snackbar.LENGTH_LONG)
                        Log.v("coins", message)
                    }
                    binding.swipeRefresh.isRefreshing = false

                }

                is Resource.Loading -> {

                    visibleShimmer()


                }
            }
        })

    }

    private fun visibleShimmer() {

        binding.layoutNews.root.visibility = View.GONE
        binding.ItemNews.root.visibility = View.GONE
        shimmerFrameLayout?.stopShimmer()
        binding.shimmerView.visibility = View.VISIBLE
        binding.shimmerRecycler.visibility = View.VISIBLE

    }

    private fun goneShimmer() {

        binding.swipeRefresh.isRefreshing = false
        binding.layoutNews.root.visibility = View.VISIBLE
        binding.ItemNews.root.visibility = View.VISIBLE
        shimmerFrameLayout?.stopShimmer()
        binding.shimmerView.visibility = View.GONE
        binding.shimmerRecycler.visibility = View.GONE

    }

    override fun onCoinItemClicked(dataCoin: CoinsData.Data) {

        val intent = Intent(this, CoinActivity::class.java)
        intent.putExtra("dataCoin",dataCoin.coinInfo.fullName)
        intent.putExtra("price",dataCoin.dISPLAY.uSD.pRICE)
        intent.putExtra("change24hour",dataCoin.dISPLAY.uSD.cHANGE24HOUR)
        intent.putExtra("open24hour",dataCoin.dISPLAY.uSD.oPEN24HOUR)
        intent.putExtra("high24hour",dataCoin.dISPLAY.uSD.hIGH24HOUR)
        intent.putExtra("algorithm",dataCoin.coinInfo.algorithm)
        intent.putExtra("totalvolume24h",dataCoin.dISPLAY.uSD.tOTALVOLUME24H)
        startActivity(intent)

    }
}