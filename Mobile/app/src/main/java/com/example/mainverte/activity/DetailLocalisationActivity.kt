package com.example.mainverte.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import com.example.mainverte.models.Balise
import com.example.mainverte.room.models.BaliseFav
import kotlinx.android.synthetic.main.activity_info_balise.*

class DetailLocalisationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_localisation)
    }
}

