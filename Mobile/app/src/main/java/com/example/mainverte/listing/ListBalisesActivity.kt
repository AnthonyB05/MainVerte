package com.example.mainverte.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mainverte.R
import com.example.mainverte.adapter.AdapterBalise
import com.example.mainverte.api.Api
import com.example.mainverte.models.Balise
import com.example.mainverte.models.ListBalises
import com.example.mainverte.utils.Constant
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_list_balises.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListBalisesActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_balises)
        var data = Api.apiService.getBalises(Constant.token)
        data.enqueue(object : Callback<ListBalises> {
            override fun onResponse(call: Call<ListBalises>, response: Response<ListBalises>) {
                if (response.isSuccessful){
                    recyclerViewBalise.adapter = AdapterBalise(this@ListBalisesActivity, response.body()!!.balises!!)
                }
            }
            override fun onFailure(call: Call<ListBalises>, t: Throwable) {
                Toast.makeText(this@ListBalisesActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}