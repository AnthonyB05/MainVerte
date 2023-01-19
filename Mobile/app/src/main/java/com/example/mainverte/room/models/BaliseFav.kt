package com.example.mainverte.room.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "balise_fav")

data class BaliseFav(
    @ColumnInfo(name = "nameBalise")
    val nameBalise: String,
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long = 0
)
