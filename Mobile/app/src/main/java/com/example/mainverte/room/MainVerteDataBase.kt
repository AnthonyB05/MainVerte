package com.example.mainverte.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mainverte.room.DAO.BaliseFavDao
import com.example.mainverte.room.DAO.ParameterDao
import com.example.mainverte.room.models.BaliseFav
import com.example.mainverte.room.models.Parameter
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [BaliseFav::class, Parameter::class], version = 2)
public abstract class MainVerteDataBase : RoomDatabase() {

    abstract fun baliseDao(): BaliseFavDao
    abstract fun parameterDao(): ParameterDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MainVerteDataBase? = null

        fun getDatabase(
            context: Context,
        ): MainVerteDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainVerteDataBase::class.java,
                    "main_verte_database"
                )
                    .fallbackToDestructiveMigration()
                    //.addCallback(BaliseDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class BaliseDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var baliseFavDao = database.baliseDao()

                    // Delete all content here.
                    baliseFavDao.deleteAll()

                    // Add sample words.
                    val tempLatLng = LatLng(0.26526, 2.1651561)
                    var fav = BaliseFav("test")
                    baliseFavDao.insert(fav)
                    fav= BaliseFav("test4")
                    baliseFavDao.insert(fav)
                }
            }
        }
    }

}