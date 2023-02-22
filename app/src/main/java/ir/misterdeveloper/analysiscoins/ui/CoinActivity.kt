package ir.misterdeveloper.analysiscoins.ui

import android.annotation.SuppressLint
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import ir.misterdeveloper.analysiscoins.R
import ir.misterdeveloper.analysiscoins.databinding.ActivityCoinBinding


class CoinActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCoinBinding
    private var nameCoin: String? = null
    private var price: String? = null
    private var change24hour: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)



        nameCoin = intent.getStringExtra("dataCoin").toString()
        price = intent.getStringExtra("price").toString()
        change24hour = intent.getStringExtra("change24hour")

        binding.layoutToolbar.toolbar.title = nameCoin



        initChartUi()
        initStatisticsUi()


    }



    @SuppressLint("SetTextI18n")
    private fun initChartUi() {



        binding.layoutChart.textViewChartPrice.text = price
        binding.layoutChart.textViewChartChange1.text = " " + change24hour!!

        if (nameCoin == "BUSD") {
            binding.layoutChart.textViewChartChange2.text = "0%"
        } else {
            binding.layoutChart.textViewChartChange2.text =
                change24hour!!.substring(0, 5) + "%"
        }

        val taghir = change24hour
        if (taghir!!.contains("-")) {

            binding.layoutChart.textViewChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.layoutChart.textViewChartUpDown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorLoss
                )
            )

            binding.layoutChart.textViewChartUpDown.text = "▼"


        } else {

            binding.layoutChart.textViewChartChange2.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.layoutChart.textViewChartUpDown.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.colorGain
                )
            )

            binding.layoutChart.textViewChartUpDown.text = "▲"


        }


    }




    @SuppressLint("SetTextI18n")
    private fun initStatisticsUi() {

        binding.layoutStatistics.textViewOpenAmount.text = intent.getStringExtra("high24hour")
        binding.layoutStatistics.textViewTodaysHighAmount.text = intent.getStringExtra("open24hour")
        binding.layoutStatistics.textViewAlgorithm.text = intent.getStringExtra("algorithm")
        binding.layoutStatistics.textViewTotalVolume.text = intent.getStringExtra("totalvolume24h")


    }


}