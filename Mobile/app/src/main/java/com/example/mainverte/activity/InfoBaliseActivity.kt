package com.example.mainverte.activity

import android.graphics.Color
import android.graphics.Color.green
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
import com.example.mainverte.utils.Constant
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.android.synthetic.main.activity_info_balise.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class InfoBaliseActivity : AppCompatActivity() {

    private val baliseViewModel: BaliseFavViewModel by viewModels {
        BaliseFavViewModelFactory((this.application as MainVerteApplication).repository)
    }
    private var balise: Balise? = null
    private var listBaliseData: ArrayList<BalisesData>? = null
    private lateinit var lineChartTemp: LineChart
    private lateinit var lineChartHumid: LineChart
    private lateinit var lineChartLum: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_balise)
        lineChartTemp = findViewById(R.id.lineChartTemp)
        lineChartHumid = findViewById(R.id.lineChartHumid)
        lineChartLum = findViewById(R.id.lineChartLum)

        val intent = intent
        this.balise = intent.getParcelableExtra("balise")
        if (balise != null) {
            textViewNameBalise.text = balise!!.nameBalise
            var data = Api.apiService.getBaliseDataById(Constant.token,balise!!.id)
            data.enqueue(object : Callback<ListData> {
                override fun onResponse(call: Call<ListData>, response: Response<ListData>) {
                    if (response.isSuccessful) {
                        listBaliseData = response.body()!!.balisesData
                        if (listBaliseData!!.size > 0) {
                            textViewInfoTemp.text =
                                listBaliseData!![0].degreCelsius.toString() + "°C"
                            textViewInfoHumid.text = listBaliseData!![0].humiditeExt.toString()
                            textViewInfoLum.text = listBaliseData!![0].luminosite.toString()
                            populateLineChartTemp(listBaliseData!!)
                            populateLineChartHumid(listBaliseData!!)
                            populateLineChartLum(listBaliseData!!)
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
        val listAverageTemp :  ArrayList<ArrayList<String>> = averagePerDayTemp(values)
        val listDate = ArrayList<String>()
        var i = 0
        if (listAverageTemp.size >0) {
            for (item in listAverageTemp) {
                var value = item[1].toString().toFloat()
                ourLineChartEntries.add(Entry(i.toFloat(), value))
                listDate.add(item[0])
                i++
            }
        }
        val lineDataSet = LineDataSet(ourLineChartEntries, "Température")
        lineDataSet.setColors(R.color.purple_200)
        lineDataSet.circleRadius = 10f
        lineDataSet.setDrawFilled(true)
        lineDataSet.valueTextSize = 20F
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        val data = LineData(lineDataSet)
        lineChartTemp.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChartTemp.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //Customizing x axis value
        xAxis.granularity = 1f // minimum axis-step (interval) is 1
        xAxis.valueFormatter = IndexAxisValueFormatter(listDate)
        xAxis.setAvoidFirstLastClipping(true)
        lineChartTemp.legend.isEnabled = false
        //remove description label
        lineChartTemp.description.isEnabled = false

        // background color
        lineChartTemp.setBackgroundColor(Color.WHITE)

        // enable touch gestures
        lineChartTemp.setTouchEnabled(true)

        lineChartTemp.setDrawGridBackground(false)

        // enable scaling and dragging
        lineChartTemp.setDragEnabled(true)
        lineChartTemp.setScaleEnabled(true)
        lineChartTemp.setScaleXEnabled(true);
        lineChartTemp.setScaleYEnabled(true);

        // force pinch zoom along both axis
        lineChartTemp.setScaleXEnabled(true);
        lineChartTemp.setScaleYEnabled(true);

        // force pinch zoom along both axis
        lineChartTemp.setPinchZoom(true)
        //add animation
        lineChartTemp.animateX(1000, Easing.EaseInSine)
        lineChartTemp.data = data
        //refresh
        lineChartTemp.invalidate()
    }

    private fun populateLineChartHumid(values: ArrayList<BalisesData>) {
        val ourLineChartEntries: ArrayList<Entry> = ArrayList()
        val listAverageHumid :  ArrayList<ArrayList<String>> = averagePerDayHumid(values)
        val listDate = ArrayList<String>()
        var i = 0
        if (listAverageHumid.size >0) {
            for (item in listAverageHumid) {
                var value = item[1].toString().toFloat()
                ourLineChartEntries.add(Entry(i.toFloat(), value))
                listDate.add(item[0])
                i++
            }
        }
        val lineDataSet = LineDataSet(ourLineChartEntries, "Humidité")
        lineDataSet.setColors(R.color.purple_200)
        lineDataSet.circleRadius = 10f
        lineDataSet.setDrawFilled(true)
        lineDataSet.valueTextSize = 20F
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        val data = LineData(lineDataSet)
        lineChartHumid.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChartHumid.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //Customizing x axis value
        xAxis.granularity = 1f // minimum axis-step (interval) is 1
        xAxis.valueFormatter = IndexAxisValueFormatter(listDate)
        xAxis.setAvoidFirstLastClipping(true)
        lineChartHumid.legend.isEnabled = false
        //remove description label
        lineChartHumid.description.isEnabled = false

        // background color
        lineChartHumid.setBackgroundColor(Color.WHITE)

        // enable touch gestures
        lineChartHumid.setTouchEnabled(true)

        lineChartHumid.setDrawGridBackground(false)

        // enable scaling and dragging
        lineChartHumid.setDragEnabled(true)
        lineChartHumid.setScaleEnabled(true)
        lineChartHumid.setScaleXEnabled(true);
        lineChartHumid.setScaleYEnabled(true);

        // force pinch zoom along both axis
        lineChartHumid.setScaleXEnabled(true);
        lineChartHumid.setScaleYEnabled(true);

        // force pinch zoom along both axis
        lineChartHumid.setPinchZoom(true)
        //add animation
        lineChartHumid.animateX(1000, Easing.EaseInSine)
        lineChartHumid.data = data
        //refresh
        lineChartHumid.invalidate()
    }

    private fun populateLineChartLum(values: ArrayList<BalisesData>) {
        val ourLineChartEntries: ArrayList<Entry> = ArrayList()
        val listAverageLum :  ArrayList<ArrayList<String>> = averagePerDayHumid(values)
        val listDate = ArrayList<String>()
        var i = 0
        if (listAverageLum.size >0) {
            for (item in listAverageLum) {
                var value = item[1].toString().toFloat()
                ourLineChartEntries.add(Entry(i.toFloat(), value))
                listDate.add(item[0])
                i++
            }
        }
        val lineDataSet = LineDataSet(ourLineChartEntries, "Humidité")
        lineDataSet.setColors(R.color.purple_200)
        lineDataSet.circleRadius = 10f
        lineDataSet.setDrawFilled(true)
        lineDataSet.valueTextSize = 20F
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        val data = LineData(lineDataSet)
        lineChartLum.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChartLum.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        //Customizing x axis value
        xAxis.granularity = 1f // minimum axis-step (interval) is 1
        xAxis.valueFormatter = IndexAxisValueFormatter(listDate)
        xAxis.setAvoidFirstLastClipping(true)
        lineChartLum.legend.isEnabled = false
        //remove description label
        lineChartLum.description.isEnabled = false

        // background color
        lineChartLum.setBackgroundColor(Color.WHITE)

        // enable touch gestures
        lineChartLum.setTouchEnabled(true)

        lineChartLum.setDrawGridBackground(false)

        // enable scaling and dragging
        lineChartLum.setDragEnabled(true)
        lineChartLum.setScaleEnabled(true)
        lineChartLum.setScaleXEnabled(true);
        lineChartLum.setScaleYEnabled(true);

        // force pinch zoom along both axis
        lineChartLum.setScaleXEnabled(true);
        lineChartLum.setScaleYEnabled(true);

        // force pinch zoom along both axis
        lineChartLum.setPinchZoom(true)
        //add animation
        lineChartLum.animateX(1000, Easing.EaseInSine)
        lineChartLum.data = data
        //refresh
        lineChartLum.invalidate()
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

    private fun averagePerDayHumid(values: ArrayList<BalisesData>): ArrayList<ArrayList<String>> {
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
                            total += item2.humiditeExt
                            average = total/count
                        }
                        else{
                            if (total <= 0.0){
                                average = item.humiditeExt
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

    private fun averagePerDayLum(values: ArrayList<BalisesData>): ArrayList<ArrayList<String>> {
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
                            total += item2.luminosite
                            average = total/count
                        }
                        else{
                            if (total <= 0.0){
                                average = item.luminosite
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