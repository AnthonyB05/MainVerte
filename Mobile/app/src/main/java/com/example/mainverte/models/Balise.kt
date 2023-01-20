package com.example.mainverte.models

import android.os.Parcel
import android.os.Parcelable

data class Balise(
    val idBalise: Long,
    val name: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(idBalise)
        parcel.writeString(name)
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
