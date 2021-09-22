package com.example.kookplayer.extensions

import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull

fun Cursor.getLong(columnName: String): Long?{
    try {
       return this.getLongOrNull(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        return 0
//        throw IllegalStateException("invalid column $columnName")
    }
}

fun Cursor.getString(columnName: String): String?{
    try {
        return this.getStringOrNull(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        return ""
//        throw IllegalStateException("invalid column $columnName")
    }
}

fun Cursor.getInt(columnName: String): Int?{
    try {
        return this.getIntOrNull(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        return 0
//        throw IllegalStateException("invalid column $columnName")
    }
}