package com.example.mainverte.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Balise(
    val id: Long,
    val nameBalise: String?,
    @SerializedName("longitude")
    @Expose
    var longitude: Double,
    @SerializedName("latitude")
    @Expose
    var latitude: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(nameBalise)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Balise> {
        override fun createFromParcel(parcel: Parcel): Balise {
            return Balise(parcel)
        }

        override fun newArray(size: Int): Array<Balise?> {
            return arrayOfNulls<Balise>(size)
        }
    }
}

data class ListBalises(
    val balises: ArrayList<Balise>?= null
)

data class OneBalise(
    val balise: ArrayList<Balise>?= null

    )
