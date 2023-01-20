package com.example.mainverte.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mainverte.R
import com.example.mainverte.adapter.AdapterBalise
import com.example.mainverte.adapter.AdapterBaliseLocalisation
import com.example.mainverte.models.Balise
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.Adapter
import kotlinx.android.synthetic.main.activity_list_balises.*
import kotlinx.android.synthetic.main.activity_localisation.*

class ListLocalisationActivity : AppCompatActivity() {

    private var apiListBalises: ArrayList<Balise>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localisation)
        val intent = intent
        this.apiListBalises = intent.getParcelableArrayListExtra("apiListBalises")
        if (this.apiListBalises != null){
            recyclerViewBaliseLocalisation.adapter = AdapterBaliseLocalisation(this, this.apiListBalises!!)
        }
    }
}