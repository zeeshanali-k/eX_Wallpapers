package com.techsensei.exwallpapers.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "wallpapers")
data class Wallpaper(@PrimaryKey(autoGenerate = false) val id:Int,
                     var date:Long,
                     var isFavourite:Boolean,
                     val link: String?,
                     val created: String?,
                     val category: String?,
                     val thumbnail: String?,
                     val name: String?,
                     var likes:Long=0):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeLong(date)
        parcel.writeByte(if (isFavourite) 1 else 0)
        parcel.writeString(link)
        parcel.writeString(created)
        parcel.writeString(category)
        parcel.writeString(thumbnail)
        parcel.writeString(name)
        parcel.writeLong(likes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Wallpaper> {
        override fun createFromParcel(parcel: Parcel): Wallpaper {
            return Wallpaper(parcel)
        }

        override fun newArray(size: Int): Array<Wallpaper?> {
            return arrayOfNulls(size)
        }
    }
}