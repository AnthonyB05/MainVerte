package com.example.mainverte.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mainverte.R
import com.example.mainverte.adapter.AdapterBalise
import com.example.mainverte.models.Balise
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_list_balises.*

class ListBalisesActivity : AppCompatActivity() {

    private var apiListBalises: ArrayList<Balise>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_balises)
        val intent = intent
        this.apiListBalises = intent.getParcelableArrayListExtra("apiListBalises")
        if (this.apiListBalises != null){
            recyclerViewBalise.adapter = AdapterBalise(this, this.apiListBalises!!)
        }

    }
}