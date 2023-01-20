package com.example.mainverte.balisesFavRecyclerView

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.mainverte.room.models.BaliseFav

class BaliseFavAdapter : ListAdapter<BaliseFav, BaliseFavViewHolder>(BaliseFavComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaliseFavViewHolder {
        return BaliseFavViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: BaliseFavViewHolder, position: Int) {
        val current: BaliseFav = getItem(position)
        holder.bind(current)
    }
}

class BaliseFavComparator : DiffUtil.ItemCallback<BaliseFav>() {
    override fun areItemsTheSame(oldItem: BaliseFav, newItem: BaliseFav): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: BaliseFav, newItem: BaliseFav): Boolean {
        return oldItem.id == newItem.id
    }
}
