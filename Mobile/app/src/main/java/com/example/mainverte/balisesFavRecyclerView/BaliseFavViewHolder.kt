package com.example.mainverte.balisesFavRecyclerView

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mainverte.R
import com.example.mainverte.activity.InfoBaliseActivity
import com.example.mainverte.models.Balise
import com.example.mainverte.room.MainVerteDataBase
import com.example.mainverte.room.models.BaliseFav
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BaliseFavViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var executor: ExecutorService = Executors.newSingleThreadExecutor()

    private val nameBalise: TextView = itemView.findViewById(R.id.textViewNameBalise)
    val buttonSupp: ImageButton = itemView.findViewById(R.id.buttonSupp)
    val buttonMaps: ImageButton = itemView.findViewById(R.id.buttonMaps)
    val buttonDetail: ImageButton = itemView.findViewById(R.id.imageButtonDetail)

    fun bind(baliseFav: BaliseFav) {
        nameBalise.text = baliseFav.nameBalise
        buttonSupp.setOnClickListener {
            executor.execute {
                MainVerteDataBase.getDatabase(itemView.context).baliseDao().delete(baliseFav)
            }
        }
        buttonMaps.setOnClickListener {
            //TODO
        }
        buttonDetail.setOnClickListener{
            val temp = Balise(baliseFav.id, baliseFav.nameBalise)
            val intent: Intent = Intent(itemView.context, InfoBaliseActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("balise", temp)
            intent.putExtras(bundle)
            itemView.context.startActivity(intent)
        }
    }

    companion object {
        fun create(parent: ViewGroup): BaliseFavViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_balise_fav_liste, parent, false)
            return BaliseFavViewHolder(view)
        }
    }
}