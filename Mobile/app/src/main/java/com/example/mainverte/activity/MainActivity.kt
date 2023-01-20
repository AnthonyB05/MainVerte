package com.example.mainverte.activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.media.audiofx.Equalizer.Settings
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mainverte.R
import com.example.mainverte.listing.ListBalisesActivity
import com.example.mainverte.listing.ListBalisesFavActivity
import com.example.mainverte.listing.ListBalisesParameterActivity
import com.example.mainverte.listing.ListLocalisationActivity
import com.example.mainverte.models.Balise
import com.example.mainverte.models.Data
import com.example.mainverte.utils.Network
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: LatLng? = null
    private var apiListBalises: ArrayList<Balise>? = null
    private var apiListBalisesData: ArrayList<Data>? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_default,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menuAbout -> {
                Toast.makeText(this,"test", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifInternet()
        //récupération de la localisation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation();
        // gestion des boutons
        buttonMaps.setOnClickListener {
            val intentMaps: Intent = Intent(this@MainActivity, MapsActivity::class.java);
            if (this.currentLocation != null){
                val bundle = Bundle()
                bundle.putParcelable("currentLocation", this.currentLocation)
                intentMaps.putExtras(bundle)
            }
            startActivity(intentMaps);
        }
        buttonRefresh.setOnClickListener {
            verifInternet()
        }
        buttonBalise.setOnClickListener{
            val intent: Intent = Intent(this@MainActivity, ListBalisesActivity::class.java)
            if (this.apiListBalises != null){
                val bundle = Bundle()
                bundle.putParcelableArrayList("apiListBalises", this.apiListBalises)
                intent.putExtras(bundle)
            }
            startActivity(intent)
        }
        buttonParameter.setOnClickListener {
            val intent: Intent = Intent(this@MainActivity, ListBalisesParameterActivity::class.java)
            if (this.apiListBalises != null){
                val bundle = Bundle()
                bundle.putParcelableArrayList("apiListBalises", this.apiListBalises)
                intent.putExtras(bundle)
            }
            startActivity(intent)
        }
        buttonFav.setOnClickListener {
            val intent: Intent = Intent(this@MainActivity, ListBalisesFavActivity::class.java)
            startActivity(intent)
        }
        buttonLocalisation.setOnClickListener {
            val intent: Intent = Intent(this@MainActivity, ListLocalisationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getCurrentLocation(){
        if (checkPermissions()){
            if (isLocationEnabled()){
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
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task ->
                    val location: Location? = task.result
                    if (location == null){
                        Toast.makeText(this, "Null Recieved", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(this, "Get Success", Toast.LENGTH_SHORT).show()
                        val coord = LatLng(location.latitude, location.longitude)
                        this.currentLocation = coord
                    }

                }
            }
            else{
                Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }
        else{
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object{
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
        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
                getCurrentLocation()
            }
            else{
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifInternet(){
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->

        }
        if (!Network.isNetworkAvailable(this)){
            val builder = AlertDialog.Builder(this)
            with(builder)
            {
                setTitle("Alert")
                setMessage("Vous devez être connecté")
                setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
                show()
            }
            visibleRefreshButton(false)
        }
        else {
            visibleRefreshButton(true)

        }
    }



    private fun visibleRefreshButton(result: Boolean) {
        if (result){
            buttonMaps.visibility = View.VISIBLE;
            buttonFav.visibility = View.VISIBLE;
            buttonBalise.visibility = View.VISIBLE
            buttonLocalisation.visibility = View.VISIBLE
            buttonParameter.visibility = View.VISIBLE
            buttonRefresh.visibility = View.GONE
        }
        else{
            buttonMaps.visibility = View.GONE;
            buttonFav.visibility = View.GONE;
            buttonBalise.visibility = View.GONE
            buttonLocalisation.visibility = View.GONE
            buttonParameter.visibility = View.GONE
            buttonRefresh.visibility = View.VISIBLE
        }
    }
}