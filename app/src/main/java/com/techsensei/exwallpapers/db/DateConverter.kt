package com.techsensei.exwallpapers.db

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun toTimeStamp(date:Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(date:Long?): Date? {
        return date?.let { Date(it) }
    }
}