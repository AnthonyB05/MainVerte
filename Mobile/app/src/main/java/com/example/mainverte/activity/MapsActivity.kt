package com.example.mainverte.activity


import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mainverte.R
import com.example.mainverte.api.Api
import com.example.mainverte.databinding.ActivityMapsBinding
import com.example.mainverte.listing.ListBalisesActivity
import com.example.mainverte.models.Balise
import com.example.mainverte.models.ListBalises
import com.example.mainverte.models.ListData
import com.example.mainverte.models.OneBaliseData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var currentLocation: LatLng? = null
    private var apiListBalises: ArrayList<Balise>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val intent = intent
        this.currentLocation = intent.getParcelableExtra<LatLng>("currentLocation")
        this.apiListBalises = intent.getParcelableArrayListExtra("apiListbalises")
        // gestion des tab
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.text!!.equals("Liste balises")) {
                    val intent: Intent = Intent(this@MapsActivity, ListBalisesActivity::class.java);
                    if (apiListBalises != null) {
                        val bundle = Bundle()
                        bundle.putParcelableArrayList("apiListBalises", apiListBalises)
                        intent.putExtras(bundle)
                    }
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

        // Add a marker in Sydney and move the camera
        //current location
        if (this.currentLocation != null) {
            mMap.addMarker(
                MarkerOptions().position(this.currentLocation!!).title("Moi")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation!!, 20F))
        }
        if (this.apiListBalises != null) {
            var i = 0
            while (i < this.apiListBalises!!.size) {
                var tempName = apiListBalises!![i].nameBalise
                var data = Api.apiService.getLastBaliseDataById(this.apiListBalises!![i].id)
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
                        Toast.makeText(applicationContext, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
                i++
            }
        }
    }

}