package com.example.mainverte.balisesFavRecyclerView

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mainverte.room.models.BaliseFav
import com.example.mainverte.room.repository.BaliseFavRepository
import kotlinx.coroutines.launch

class BaliseFavViewModel(private val repository: BaliseFavRepository) : ViewModel(){

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allBalises: LiveData<List<BaliseFav>> = repository.allBalisesFav

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(baliseFav: BaliseFav) = viewModelScope.launch {
        repository.insert(baliseFav)
    }

    fun delete(baliseFav: BaliseFav) = viewModelScope.launch {
        repository.delete(baliseFav)
    }
}
class BaliseFavViewModelFactory(private val repository: BaliseFavRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BaliseFavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BaliseFavViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}