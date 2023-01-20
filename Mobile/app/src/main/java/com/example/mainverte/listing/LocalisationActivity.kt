package com.example.mainverte.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mainverte.R
import com.example.mainverte.adapter.AdapterBalise
import com.example.mainverte.models.Balise
import kotlinx.android.synthetic.main.activity_list_balises.*

class LocalisationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localisation)
        val listBalise: ArrayList<Balise> = ArrayList()
        val balise = Balise("test")
        listBalise.add(balise)
        recyclerViewBalise.adapter = AdapterBalise(this, listBalise!!)
    }
}