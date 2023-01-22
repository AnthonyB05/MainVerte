package com.example.mainverte.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.api.Api
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import com.example.mainverte.models.*
import com.example.mainverte.room.models.BaliseFav
import kotlinx.android.synthetic.main.activity_info_balise.*
import kotlinx.coroutines.*

import retrofit2.awaitResponse

class InfoBaliseActivity : AppCompatActivity() {

    private val baliseViewModel: BaliseFavViewModel by viewModels {
        BaliseFavViewModelFactory((this.application as MainVerteApplication).repository)
    }
    private var balise : Balise? = null
    private var listBaliseData: ArrayList<BalisesData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_balise)
        val intent = intent
        this.balise = intent.getParcelableExtra("balise")
        this.listBaliseData = intent.getParcelableArrayListExtra("listBaliseData")
        if (balise != null ){
            textViewNameBalise.text = balise!!.nameBalise
            GlobalScope.launch {
                getCurrentBalise(balise!!.id)
                if (listBaliseData != null) {
                    textViewInfoTemp.text = listBaliseData!![0].degreCelsius.toString() + "°C"
                    textViewInfoHumid.text = listBaliseData!![0].humiditeExt.toString()
                    textViewInfoLum.text = listBaliseData!![0].luminosite.toString()
                }
            }
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


    private suspend fun getCurrentBalise(id: Long) {
       try{
           val response = Api.apiService.getBaliseDataById(id).awaitResponse()
           if (response.isSuccessful){
               listBaliseData = response.body()!!.balisesData
           }
       }
       catch (t: Throwable){
            Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
       }
    }

}