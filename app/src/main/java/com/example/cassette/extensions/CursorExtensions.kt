package com.example.cassette.extensions

import android.database.Cursor

fun Cursor.getLong(columnName: String): Long{
    try {
       return this.getLong(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        return 0
//        throw IllegalStateException("invalid column $columnName")
    }
}

fun Cursor.getString(columnName: String): String{
    try {
        return this.getString(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        return ""
//        throw IllegalStateException("invalid column $columnName")
    }
}

fun Cursor.getInt(columnName: String): Int{
    try {
        return this.getInt(this.getColumnIndexOrThrow(columnName))
    }
    catch (exception : Exception)
    {
        return 0
//        throw IllegalStateException("invalid column $columnName")
    }
}