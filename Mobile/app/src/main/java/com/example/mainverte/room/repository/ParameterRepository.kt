package com.example.mainverte.room.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.mainverte.room.DAO.BaliseFavDao
import com.example.mainverte.room.DAO.ParameterDao
import com.example.mainverte.room.models.BaliseFav
import com.example.mainverte.room.models.Parameter

//Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ParameterRepository(private val parameterDao: ParameterDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allParameter: LiveData<List<Parameter>> = parameterDao.getAll()


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(parameter: Parameter) {
        parameterDao.insert(parameter)
    }

}