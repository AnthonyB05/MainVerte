package com.example.mainverte.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.api.Api
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import com.example.mainverte.models.Balise
import com.example.mainverte.models.BalisesData
import com.example.mainverte.models.ListBalises
import com.example.mainverte.models.OneBaliseData
import com.example.mainverte.room.models.BaliseFav
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_detail_localisation.*
import kotlinx.android.synthetic.main.activity_info_balise.*
import kotlinx.android.synthetic.main.activity_info_balise.textViewNameBalise
import kotlinx.android.synthetic.main.item_balise_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailLocalisationActivity : AppCompatActivity() {

    private var balise : Balise? = null
    private var baliseData: BalisesData? = null
    private var currentLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_localisation)

        val intent = intent
        this.balise = intent.getParcelableExtra("balise")
        this.currentLocation = intent.getParcelableExtra("currentLocation")
        if (this.balise != null){
            textViewNameBalise.text = this.balise!!.nameBalise
            getLastBaliseData(this.balise!!.id)

            buttonLocation.setOnClickListener {
                if (this.currentLocation != null){
                    if (this.baliseData != null){
                        this.baliseData!!.latitude = this.currentLocation!!.latitude
                        this.baliseData!!.longitude = this.currentLocation!!.longitude
                        createBaliseData(this.baliseData!!)
                    }
                }
                else{
                    Toast.makeText(this, "Erreur, veuillez activer votre localisation", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createBaliseData(balisesData: BalisesData) {

        var data = Api.apiService.createBaliseData(balisesData)
        data.enqueue(object : Callback<BalisesData>{
            override fun onResponse(call: Call<BalisesData>, response: Response<BalisesData>) {
                if (response.isSuccessful){
                    Toast.makeText(this@DetailLocalisationActivity, "Mise à jour de la localisation avec succès", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BalisesData>, t: Throwable) {
                Toast.makeText(this@DetailLocalisationActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getLastBaliseData(id: Long) {

        var data = Api.apiService.getLastBaliseDataById(id)
        data.enqueue(object : Callback<OneBaliseData>{
            override fun onResponse(call: Call<OneBaliseData>, response: Response<OneBaliseData>) {
                if (response.isSuccessful){
                    baliseData = response.body()!!.balisesData
                }
            }
            override fun onFailure(call: Call<OneBaliseData>, t: Throwable) {
                Toast.makeText(this@DetailLocalisationActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }
}

