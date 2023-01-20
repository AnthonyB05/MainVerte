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

    private var apiListBalises: ArrayList<Balise>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_balises_parameter)
        val intent = intent
        this.apiListBalises = intent.getParcelableArrayListExtra("apiListBalises")
        if (this.apiListBalises != null){
            recyclerViewParameter.adapter = AdapterBaliseParameter(this, this.apiListBalises!!)
        }
    }
}