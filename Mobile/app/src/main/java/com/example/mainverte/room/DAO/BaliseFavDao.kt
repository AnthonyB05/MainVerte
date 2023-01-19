package com.example.mainverte.room.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mainverte.room.models.BaliseFav

@Dao
interface BaliseFavDao {
    @Query("SELECT * FROM balise_fav ORDER BY nameBalise ASC")
    fun getAlphabetizedBalisesFav(): LiveData<List<BaliseFav>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(baliseFav: BaliseFav)

    @Query("DELETE FROM balise_fav")
    suspend fun deleteAll()

    @Query("DELETE FROM balise_fav " +
            "WHERE id = :id")
    fun deleteById(id: Long);
    @Delete
    fun delete(balise: BaliseFav)

}