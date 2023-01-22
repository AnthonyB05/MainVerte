package com.example.mainverte.models

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class BalisesData(
    val _id: String?,
    val idBalise: Long,
    val degreCelsius: Double,
    val humiditeExt: Double,
    val luminosite: Double,
    var longitude: Double,
    var latitude: Double,
    val date: Date
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
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
        parcel.writeString(_id)
        parcel.writeLong(idBalise)
        parcel.writeDouble(degreCelsius)
        parcel.writeDouble(humiditeExt)
        parcel.writeDouble(luminosite)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BalisesData> {
        override fun createFromParcel(parcel: Parcel): BalisesData {
            return BalisesData(parcel)
        }

        override fun newArray(size: Int): Array<BalisesData?> {
            return arrayOfNulls(size)
        }
    }
}

data class OneBaliseData(
    val balisesData: BalisesData? = null,

    )

data class ListData(
    val balisesData: ArrayList<BalisesData>?= null,
)

