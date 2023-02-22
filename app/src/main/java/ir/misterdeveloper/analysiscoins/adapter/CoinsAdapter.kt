package ir.misterdeveloper.analysiscoins.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ir.misterdeveloper.analysiscoins.R
import ir.misterdeveloper.analysiscoins.model.CoinsData
import ir.misterdeveloper.analysiscoins.util.BASE_URL_IMAGE
import java.math.RoundingMode
import java.text.DecimalFormat

class CoinsAdapter(
    private var context: Context,
    private var coinsResponse: ArrayList<CoinsData.Data>? = null,
    private val recyclerCallback: RecyclerCallback
) :
    RecyclerView.Adapter<CoinsAdapter.COINSVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinsAdapter.COINSVH {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_coins, null)
        return COINSVH(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CoinsAdapter.COINSVH, position: Int) {

        val coinsResponse = coinsResponse?.get(position)

        holder.textViewCoinName.text = coinsResponse!!.coinInfo.fullName



        holder.textViewPrice.text = coinsResponse.dISPLAY.uSD.pRICE


        val textTaghir = coinsResponse.rAW.uSD.cHANGE24HOUR
        if (textTaghir > 0) {
            holder.txtTaghir.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorGain
                )
            )
            val indexDot = coinsResponse.rAW.uSD.cHANGE24HOUR.toString().indexOf('.')

            holder.txtTaghir.text =
                coinsResponse.rAW.uSD.cHANGE24HOUR.toString().substring(0, indexDot + 3) + "%"

        } else if (textTaghir < 0) {
            holder.txtTaghir.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLoss
                )
            )

            val indexDot = coinsResponse.rAW.uSD.cHANGE24HOUR.toString().indexOf('.')
            holder.txtTaghir.text =
                coinsResponse.rAW.uSD.cHANGE24HOUR.toString().substring(0, indexDot + 3) + "%"

        } else {
            holder.txtTaghir.text = "0%"
        }

        val marketCap = coinsResponse.rAW.uSD.mKTCAP / 1000000000
        val indexDot = marketCap.toString().indexOf('.')
        holder.textViewMarketCap.text = "$" + marketCap.toString().substring(0, indexDot + 3) + " B"

        Picasso.get().load(BASE_URL_IMAGE + coinsResponse.coinInfo.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imageViewItem)


        holder.cardCoin.setOnClickListener {

            recyclerCallback.onCoinItemClicked(coinsResponse)


        }


    }

    override fun getItemCount(): Int {
        return coinsResponse!!.size
    }


    inner class COINSVH(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textViewCoinName: AppCompatTextView = itemView.findViewById(R.id.textViewCoinName)
        var txtTaghir: AppCompatTextView = itemView.findViewById(R.id.txtTaghir)
        var imageViewItem: AppCompatImageView = itemView.findViewById(R.id.imageViewItem)
        var textViewPrice: AppCompatTextView = itemView.findViewById(R.id.textViewPrice)
        var textViewMarketCap: AppCompatTextView = itemView.findViewById(R.id.textViewMarketCap)
        var cardCoin: RelativeLayout = itemView.findViewById(R.id.cardCoin)


    }


    interface RecyclerCallback {
        fun onCoinItemClicked(dataCoin: CoinsData.Data)
    }

}