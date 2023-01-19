package com.example.mainverte.room.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "parameter", indices = [Index(value = ["nameBalise"], unique = true)])

data class Parameter(
    @ColumnInfo(name = "nameBalise")
    var nameBalise: String,
    @ColumnInfo(name = "tempMin")
    val tempMin: Int,
    @ColumnInfo(name = "tempMax")
    val tempMax: Int,
    @ColumnInfo(name = "humidMin")
    val humidMin: Int,
    @ColumnInfo(name = "humidMax")
    val humidMax: Int,
    @ColumnInfo(name = "notifTemp")
    val notifTemp: Boolean,
    @ColumnInfo(name = "notifHumid")
    val notifHumid: Boolean,
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long = 0
/*    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long = 0*/
)