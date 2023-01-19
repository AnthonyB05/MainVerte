package com.example.mainverte

import android.app.Application
import com.example.mainverte.room.BaliseDataBase
import com.example.mainverte.room.repository.BaliseFavRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainVerteApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { BaliseDataBase.getDatabase(this) }
    val repository by lazy { BaliseFavRepository(database.baliseDao()) }
}