package com.example.mainverte.room.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mainverte.room.models.Parameter

@Dao
interface ParameterDao {
    @Query("SELECT * FROM parameter")
    fun getAll(): LiveData<List<Parameter>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(parameter: Parameter)

    @Query("DELETE FROM parameter")
    suspend fun deleteAll()

}