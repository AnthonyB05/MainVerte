package com.example.mainverte.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mainverte.R
import com.example.mainverte.adapter.AdapterBalise
import com.example.mainverte.adapter.AdapterBaliseLocalisation
import com.example.mainverte.api.Api
import com.example.mainverte.models.Balise
import com.example.mainverte.models.ListBalises
import com.google.android.gms.maps.model.LatLng
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
import kotlinx.android.synthetic.main.activity_list_balises.*
import kotlinx.android.synthetic.main.activity_list_balises_parameter.*
import kotlinx.android.synthetic.main.activity_localisation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListLocalisationActivity : AppCompatActivity() {

    private var currentLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localisation)
        val intent = intent
        this.currentLocation = intent.getParcelableExtra("currentLocation")
        var data = Api.apiService.getBalises()
        data.enqueue(object : Callback<ListBalises> {
            override fun onResponse(call: Call<ListBalises>, response: Response<ListBalises>) {
                if (response.isSuccessful){
                    recyclerViewBaliseLocalisation.adapter = AdapterBalise(this@ListLocalisationActivity, response.body()!!.balises!!)
                }
            }
            override fun onFailure(call: Call<ListBalises>, t: Throwable) {
                Toast.makeText(this@ListLocalisationActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}