package com.example.mainverte.activity

import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mainverte.R
import com.example.mainverte.utils.Network
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        verifInternet()
        buttonMaps.setOnClickListener {
            val intentMaps: Intent = Intent(this@MainActivity, MapsActivity::class.java);
            startActivity(intentMaps);
        }
        buttonRefresh.setOnClickListener {
            verifInternet()
        }
    }

    private fun verifInternet(){
        val positiveButtonClick = { dialog: DialogInterface, which: Int ->
            Toast.makeText(applicationContext,
                android.R.string.no, Toast.LENGTH_SHORT).show()
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
            buttonMaps.visibility =View.VISIBLE;
            buttonRefresh.visibility = View.GONE
        }
        else{
            buttonMaps.visibility =View.GONE;
            buttonRefresh.visibility = View.VISIBLE
        }
    }
}