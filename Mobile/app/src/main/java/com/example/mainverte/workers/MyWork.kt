package com.example.mainverte.workers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.api.Api
import com.example.mainverte.models.BalisesData
import com.example.mainverte.models.OneBaliseData
import com.example.mainverte.parameterViewModel.ParameterViewModel
import com.example.mainverte.room.models.Parameter
import com.example.mainverte.utils.Constant
import com.example.mainverte.workers.WorkerUtils.makeStatusNotification
import com.example.mainverte.workers.WorkerUtils.sleep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

private const val TAG = "MainVerte"

class MyWork(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    private val parameterViewModel: ParameterViewModel? =
        ParameterViewModel((ctx as MainVerteApplication).parameterRepository)
    private var parameterWithNotif: List<Parameter>? = null

    override fun doWork(): Result {
        val appContext = applicationContext
        //makeStatusNotification("1","Attention test", "Attention",appContext)
        // This is an utility function added to emulate slower work.
        sleep()
        return try {
            parameterWithNotif = parameterViewModel!!.getParametersWithNotif()
            if (!parameterWithNotif.isNullOrEmpty()) {
                GlobalScope.launch {
                    for (item in parameterWithNotif!!) {
                        var i = 0
                        try {
                            val response =
                                Api.apiService.getLastBaliseDataById(Constant.token, item.idBalise)
                            if (response.isSuccessful && response.body() != null) {
                                val infoBalise = response.body()!!.balisesData
                                if (infoBalise != null) {
                                    if (item.notifTemp) {
                                        if (infoBalise.degreCelsius <= item.tempMin) {
                                            makeStatusNotification(
                                                i.toString(),
                                                "La température de la balise : " + item.nameBalise + " est à " + infoBalise.degreCelsius + "°C",
                                                "Attention",
                                                appContext
                                            )
                                            i++
                                        }
                                        if (infoBalise.degreCelsius >= item.tempMax) {
                                            makeStatusNotification(
                                                i.toString(),
                                                "La température de la balise : " + item.nameBalise + " est à " + infoBalise.degreCelsius + "°C",
                                                "Attention",
                                                appContext
                                            )
                                            i++
                                        }
                                    }
                                    if (item.notifHumid) {
                                        if (infoBalise.humiditeExt <= item.humidMin) {
                                            makeStatusNotification(
                                                i.toString(),
                                                "L'humidité de la balise : " + item.nameBalise + " est à " + infoBalise.humiditeExt + "%",
                                                "Attention",
                                                appContext
                                            )
                                            i++
                                        }
                                        if (infoBalise.humiditeExt >= item.humidMax) {
                                            makeStatusNotification(
                                                i.toString(),
                                                "L'humidité de la balise : " + item.nameBalise + " est à " + infoBalise.humiditeExt + "%",
                                                "Attention",
                                                appContext
                                            )
                                            i++
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    appContext,
                                    "Error Occurred: ${response.message()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(
                                appContext,
                                "Error Occurred: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        i++
                    }
                }
            }

            Result.success()
        } catch (throwable: Throwable) {
            Log.e(TAG, "Error applying blur")
            throwable.printStackTrace()
            Result.failure()
        }
    }
}

/*
    private suspend fun getBaliseDataById(id: Long) {
        try{
            val response = Api.apiService.getLastBaliseDataById(Constant.token,id).awaitResponse()
            if (response.isSuccessful){
                infoBalise = response.body()!!.balisesData
            }
        }
        catch (t: Throwable){
            Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
        }
    }
}*/
