package com.example.mainverte.listing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mainverte.MainVerteApplication
import com.example.mainverte.R
import com.example.mainverte.balisesFavRecyclerView.BaliseFavAdapter
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModel
import com.example.mainverte.balisesFavRecyclerView.BaliseFavViewModelFactory
import kotlinx.android.synthetic.main.activity_list_balises_fav.*

class ListBalisesFavActivity : AppCompatActivity() {

    private val baliseFavViewModel: BaliseFavViewModel by viewModels {
        BaliseFavViewModelFactory((this.application as MainVerteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_balises_fav)

        val adapter = BaliseFavAdapter()
        recyclerViewFav.adapter = adapter
        recyclerViewFav.layoutManager = LinearLayoutManager(this)
        baliseFavViewModel.allBalises.observe(this) { balises ->
            // Update the cached copy of the words in the adapter.
            balises.let { adapter.submitList(it) }
        }
    }
}