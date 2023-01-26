package com.example.mainverte.activity


import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mainverte.R
import com.example.mainverte.api.Api
import com.example.mainverte.databinding.ActivityMapsBinding
import com.example.mainverte.listing.ListBalisesActivity
import com.example.mainverte.models.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.maps.android.clustering.ClusterManager
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.awaitResponse


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentLocation: LatLng? = null
    private var apiListBalises: ArrayList<Balise>? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //récupération de la localisation
        /*fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation();*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val intent = intent
        this.currentLocation = intent.getParcelableExtra<LatLng>("currentLocation")
        //this.apiListBalises = intent.getParcelableArrayListExtra("apiListbalises")
        // gestion des tab
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.text!!.equals("Liste balises")) {
                    val intent: Intent = Intent(this@MapsActivity, ListBalisesActivity::class.java);
                    startActivity(intent);
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        //add markercurrent location
        if (this.currentLocation != null) {
            mMap.addMarker(
                MarkerOptions().position(this.currentLocation!!).title("Moi")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 20F))
        }

        GlobalScope.launch {
            getCurrentBalise()
            if (apiListBalises != null) {
                var i = 0
                while (i < apiListBalises!!.size) {
                    var tempName = apiListBalises!![i].nameBalise
                    var data = Api.apiService.getLastBaliseDataById(apiListBalises!![i].id)
                    data.enqueue(object : Callback<OneBaliseData> {
                        override fun onResponse(
                            call: Call<OneBaliseData>,
                            response: retrofit2.Response<OneBaliseData>
                        ) {
                            var temp = response.body()
                            if (response.code().equals(200)) {
                                val data = temp!!.balisesData
                                val latLng = LatLng(data!!.latitude, data!!.longitude)
                                if (currentLocation != null){
                                    mMap.addMarker(MarkerOptions().position(latLng).title(tempName))
                                }
                                else{
                                    mMap.addMarker(MarkerOptions().position(latLng).title(tempName))
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 20F))
                                }
                            }
                        }

                        override fun onFailure(call: Call<OneBaliseData>, t: Throwable) {
                            Toast.makeText(this@MapsActivity, t.message, Toast.LENGTH_SHORT).show()
                        }

                    })
                    i++
                }
            }
        }
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
                        Toast.makeText(this, "Get Success", Toast.LENGTH_SHORT).show()
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

    private suspend fun getCurrentBalise() {
        try{
            val response = Api.apiService.getBalises().awaitResponse()
            if (response.isSuccessful){
                apiListBalises = response.body()!!.balises
            }
        }
        catch (t: Throwable){
            Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show()
        }
    }

}