package com.example.mainverte.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Data(
    val id: Long,
    val idBalise: Long,
    val degreCelcius: Double,
    val humiditeExt: Double,
    val luminosite: Double,
    val longitude: Double,
    val latitude: Double,
    val date: Date
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        TODO("date")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeLong(idBalise)
        parcel.writeDouble(degreCelcius)
        parcel.writeDouble(humiditeExt)
        parcel.writeDouble(luminosite)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }
}
