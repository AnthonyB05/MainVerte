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
    private val baliseViewModel: BaliseFavViewModel by viewModels {
        BaliseFavViewModelFactory((this.application as MainVerteApplication).repository)
    }
    private var balise : Balise? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_localisation)
        val intent = intent
        this.balise = intent.getParcelableExtra("balise")
        if (balise != null){
            textViewNameBalise.text = balise!!.nameBalise
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
}}