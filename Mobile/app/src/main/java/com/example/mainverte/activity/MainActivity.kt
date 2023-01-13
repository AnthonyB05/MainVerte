package com.example.mainverte.activity

import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mainverte.R
import com.example.mainverte.utils.Network
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

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
            buttonRefresh.visibility = View.GONE
        }
        else{
            buttonMaps.visibility = View.GONE;
            buttonFav.visibility = View.GONE;
            buttonBalise.visibility = View.GONE
            buttonLocalisation.visibility = View.GONE
            buttonRefresh.visibility = View.VISIBLE
        }
    }
}