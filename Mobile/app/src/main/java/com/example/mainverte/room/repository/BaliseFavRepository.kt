package com.example.mainverte.room.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.mainverte.room.DAO.BaliseFavDao
import com.example.mainverte.room.models.BaliseFav

//Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class BaliseFavRepository(private val baliseFavDao: BaliseFavDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allBalisesFav: LiveData<List<BaliseFav>> = baliseFavDao.getAlphabetizedBalisesFav()

    fun deleteById(id: Long) {
        baliseFavDao.deleteById(id);
    }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(baliseFav: BaliseFav) {
        baliseFavDao.insert(baliseFav)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun delete(baliseFav: BaliseFav) {
        baliseFavDao.delete(baliseFav)
    }
}