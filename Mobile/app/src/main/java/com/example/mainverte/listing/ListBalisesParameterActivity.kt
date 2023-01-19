package com.example.mainverte.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mainverte.R
import com.example.mainverte.adapter.AdapterBalise
import com.example.mainverte.adapter.AdapterBaliseParameter
import com.example.mainverte.models.Balise
import kotlinx.android.synthetic.main.activity_list_balises.*
import kotlinx.android.synthetic.main.activity_list_balises_parameter.*

class ListBalisesParameterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_balises_parameter)
        val listBalise: ArrayList<Balise> = ArrayList()
        val balise = Balise(1,"test")
        val balise2 = Balise(1,"test2")
        listBalise.add(balise)
        listBalise.add(balise2)
        recyclerViewParameter.adapter = AdapterBaliseParameter(this, listBalise!!)

    }
}