package com.techsensei.exwallpapers.db

import androidx.room.TypeConverter
import java.math.BigInteger

class BigIntConverter {
    @TypeConverter
    fun toIntString(likes:BigInteger)= if (likes.equals(0)) "0" else likes.toString()

    @TypeConverter
    fun toBigInt(likes:String)=likes.toBigInteger()
}