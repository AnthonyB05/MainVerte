package com.example.mainverte.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import com.example.mainverte.models.Balise
import com.example.mainverte.room.models.BaliseFav
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_info_balise.*

class InfoBaliseActivity : AppCompatActivity() {

    private val baliseViewModel: BaliseFavViewModel by viewModels {
        BaliseFavViewModelFactory((this.application as MainVerteApplication).repository)
    }
    private var balise : Balise? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_balise)
        val intent = intent
        this.balise = intent.getParcelableExtra("balise")
        if (balise != null){
            textViewNameBalise.text = balise!!.name
            // action button
            imageButtonFavoris.setOnClickListener {
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle(getString(R.string.attention))
                //set message for alert dialog
                builder.setMessage(getString(R.string.ajout_en_favoris, this.balise!!.name))

                //performing positive action
                builder.setPositiveButton(R.string.oui) { dialogInterface, which ->
                    val tempFav = BaliseFav(this.balise!!.name!!)
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
    }
}