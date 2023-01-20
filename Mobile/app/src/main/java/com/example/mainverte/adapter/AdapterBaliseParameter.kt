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
import com.example.mainverte.activity.ParameterActivity
import com.example.mainverte.models.Balise

class AdapterBaliseParameter(private val context: Context, private val list: ArrayList<Balise>) : RecyclerView.Adapter<AdapterBaliseParameter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNameBalise = itemView.findViewById(R.id.textViewNameBalise) as TextView

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_balise_parameter_liste, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.textViewNameBalise.setText(item.name)
        holder.itemView.setOnClickListener {
            val intent: Intent = Intent(context, ParameterActivity::class.java)
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