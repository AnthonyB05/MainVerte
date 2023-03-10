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
import com.example.mainverte.activity.InfoBaliseActivity
import com.example.mainverte.activity.MapsActivity
import com.example.mainverte.listing.ListBalisesActivity

import com.example.mainverte.models.Balise

class AdapterBalise(private val context: Context, private val list: ArrayList<Balise>) : RecyclerView.Adapter<AdapterBalise.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNameBalise = itemView.findViewById(R.id.textViewNameBalise) as TextView
        val buttonMaps = itemView.findViewById(R.id.buttonMaps) as ImageButton
        val buttonInfo = itemView.findViewById(R.id.buttonInfo) as ImageButton

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_balise_liste, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.textViewNameBalise.setText(item.nameBalise)
        holder.buttonMaps.setOnClickListener {
            val intent: Intent = Intent(context, MapsActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("zoomBalise", item)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
        holder.buttonInfo.setOnClickListener {
            val intent: Intent = Intent(context, InfoBaliseActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("balise", item)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}