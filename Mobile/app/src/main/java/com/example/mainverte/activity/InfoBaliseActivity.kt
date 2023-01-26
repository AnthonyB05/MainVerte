package com.example.mainverte.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.api.Api
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import com.example.mainverte.models.*
import com.example.mainverte.room.models.BaliseFav
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_info_balise.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class InfoBaliseActivity : AppCompatActivity() {

    private val baliseViewModel: BaliseFavViewModel by viewModels {
        BaliseFavViewModelFactory((this.application as MainVerteApplication).repository)
    }
    private var balise: Balise? = null
    private var listBaliseData: ArrayList<BalisesData>? = null
    private lateinit var lineChartTemp: LineChart


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_balise)
        lineChartTemp = findViewById(R.id.lineChartTemp)
        val intent = intent
        this.balise = intent.getParcelableExtra("balise")
        if (balise != null) {
            textViewNameBalise.text = balise!!.nameBalise
            var data = Api.apiService.getBaliseDataById(balise!!.id)
            data.enqueue(object : Callback<ListData> {
                override fun onResponse(call: Call<ListData>, response: Response<ListData>) {
                    if (response.isSuccessful) {
                        listBaliseData = response.body()!!.balisesData
                        if (listBaliseData != null) {
                            textViewInfoTemp.text =
                                listBaliseData!![0].degreCelsius.toString() + "°C"
                            textViewInfoHumid.text = listBaliseData!![0].humiditeExt.toString()
                            textViewInfoLum.text = listBaliseData!![0].luminosite.toString()
                            //populateLineChartTemp(listBaliseData!!)
                            averagePerDayTemp(listBaliseData!!)

                        }
                    }
                }

                override fun onFailure(call: Call<ListData>, t: Throwable) {
                    Toast.makeText(this@InfoBaliseActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
            // action button
            imageButtonFavoris.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle(getString(R.string.attention))
                //set message for alert dialog
                builder.setMessage(getString(R.string.ajout_en_favoris, this.balise!!.nameBalise))

                //performing positive action
                builder.setPositiveButton(R.string.oui) { dialogInterface, which ->
                    val tempFav = BaliseFav(this.balise!!.nameBalise!!)
                    baliseViewModel.insert(tempFav)
                }

                //performing negative action
                builder.setNegativeButton(R.string.non) { dialogInterface, which ->
                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()

                true

            }

        }
    }

    private fun populateLineChartTemp(values: ArrayList<BalisesData>) {
        val ourLineChartEntries: ArrayList<Entry> = ArrayList()

        var i = 0

        if (values != null) {
            for (item in values) {
                var value = values[i].degreCelsius.toFloat()
                ourLineChartEntries.add(Entry(i.toFloat(), value))
                i++
            }
        }
        val lineDataSet = LineDataSet(ourLineChartEntries, "")
        lineDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        val data = LineData(lineDataSet)
        lineChartTemp.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChartTemp.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        //lineChartTemp.legend

        //remove description label
        //lineChartTemp.description.isEnabled = false

        //add animation
        lineChartTemp.animateX(1000, Easing.EaseInSine)
        lineChartTemp.data = data
        //refresh
        lineChartTemp.invalidate()
    }


    private fun averagePerDayTemp(values: ArrayList<BalisesData>): ArrayList<ArrayList<String>> {
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        var count = 0
        var total = 0.00
        var average = 0.00
        var listAveragePerDay = ArrayList<ArrayList<String>>()
        var listDateFaite = ArrayList<String>()
        if (values != null)
            for (item in values.sortedBy { it.date }) {
                var tempDate = sdf.format(item.date)
                var saveDate = tempDate
                if (!listDateFaite.contains(saveDate)){
                    for (item2 in values.sortedBy { it.date }){
                        var tempDate2 = sdf.format(item2.date)
                        if (saveDate.equals(tempDate2)){
                            count++
                            total += item2.degreCelsius
                            average = total/count
                        }
                        else{
                            if (total <= 0.0){
                                average = item.degreCelsius
                            }
                            var tempList = arrayListOf<String>(saveDate, average.toString())
                            listAveragePerDay.add(tempList)
                            listDateFaite.add(saveDate)
                            count = 0
                            total = 0.0
                            average = 0.0
                            break
                        }
                    }
                }
            }
        return listAveragePerDay
    }


/*    private suspend fun getCurrentBalise(id: Long) {
       try{
           val response = Api.apiService.getBaliseDataById(id).awaitResponse()
           if (response.isSuccessful){
               listBaliseData = response.body()!!.balisesData
           }
       }
       catch (t: Throwable){
            Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
       }
    }*/

}