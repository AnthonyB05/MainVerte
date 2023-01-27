package com.example.mainverte.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.api.Api
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import com.example.mainverte.models.Balise
import com.example.mainverte.models.BalisesData
import com.example.mainverte.models.ListBalises
import com.example.mainverte.models.OneBaliseData
import com.example.mainverte.room.models.BaliseFav
import com.example.mainverte.utils.Constant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_detail_localisation.*
import kotlinx.android.synthetic.main.activity_info_balise.*
import kotlinx.android.synthetic.main.activity_info_balise.textViewNameBalise
import kotlinx.android.synthetic.main.item_balise_location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailLocalisationActivity : AppCompatActivity() {

    private var balise : Balise? = null
    private var baliseData: BalisesData? = null
    private var currentLocation: LatLng? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_localisation)
        //récupération de la localisation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation();
        val intent = intent
        this.balise = intent.getParcelableExtra("balise")
        if (this.balise != null){
            textViewNameBalise.text = this.balise!!.nameBalise
            getLastBaliseData(this.balise!!.id)

            buttonLocation.setOnClickListener {
                if (this.currentLocation != null){
                    if (this.baliseData != null){
                        this.baliseData!!.latitude = this.currentLocation!!.latitude
                        this.baliseData!!.longitude = this.currentLocation!!.longitude
                        createBaliseData(this.baliseData!!)
                    }
                }
                else{
                    Toast.makeText(this, "Erreur, veuillez activer votre localisation", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createBaliseData(balisesData: BalisesData) {

        var data = Api.apiService.createBaliseData(Constant.token,balisesData)
        data.enqueue(object : Callback<BalisesData>{
            override fun onResponse(call: Call<BalisesData>, response: Response<BalisesData>) {
                if (response.isSuccessful){
                    Toast.makeText(this@DetailLocalisationActivity, "Mise à jour de la localisation avec succès", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BalisesData>, t: Throwable) {
                Toast.makeText(this@DetailLocalisationActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getLastBaliseData(id: Long) {

        var data = Api.apiService.getLastBaliseDataById(Constant.token,id)
        data.enqueue(object : Callback<OneBaliseData>{
            override fun onResponse(call: Call<OneBaliseData>, response: Response<OneBaliseData>) {
                if (response.isSuccessful){
                    baliseData = response.body()!!.balisesData
                }
            }
            override fun onFailure(call: Call<OneBaliseData>, t: Throwable) {
                Toast.makeText(this@DetailLocalisationActivity, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getCurrentLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                // final longitude et latitude ici
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(this, "Null Recieved", Toast.LENGTH_SHORT).show()
                    } else {
                       // Toast.makeText(this, "Get Success", Toast.LENGTH_SHORT).show()
                        val coord = LatLng(location.latitude, location.longitude)
                        this.currentLocation = coord
                    }

                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

