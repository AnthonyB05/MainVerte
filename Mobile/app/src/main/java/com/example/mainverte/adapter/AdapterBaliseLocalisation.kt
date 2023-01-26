package com.example.mainverte.adapter


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mainverte.R
import com.example.mainverte.activity.DetailLocalisationActivity
import com.example.mainverte.activity.InfoBaliseActivity
import com.example.mainverte.activity.ParameterActivity
import com.example.mainverte.models.Balise
import com.google.android.gms.maps.model.LatLng

class AdapterBaliseLocalisation(private val context: Context, private val list:ArrayList<Balise>, currentLocation: LatLng?) : RecyclerView.Adapter<AdapterBaliseLocalisation.ViewHolder>() {

    private var currentLocation: LatLng? = currentLocation

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNameBaliseLocalisation = itemView.findViewById(R.id.textViewNameBaliseLocalisation) as TextView
        val buttonMaps = itemView.findViewById(R.id.buttonLocalisation) as ImageButton
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterBaliseLocalisation.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_balise_location, parent, false)

        return AdapterBaliseLocalisation.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AdapterBaliseLocalisation.ViewHolder, position: Int) {
        val item = list[position]
        holder.textViewNameBaliseLocalisation.setText(item.nameBalise)
        holder.buttonMaps.setOnClickListener {
            val intent: Intent = Intent(context, DetailLocalisationActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("balise", item)
            intent.putExtras(bundle)
/*            bundle.putParcelable("currentLocation", currentLocation)
            intent.putExtras(bundle)*/
            context.startActivity(intent)
        }

    }


}