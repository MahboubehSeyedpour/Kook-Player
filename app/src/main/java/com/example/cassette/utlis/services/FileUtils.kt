package com.example.cassette.utlis.services

import java.io.File

object FileUtils {


    fun fileType(file: File): String {
        if (file.isDirectory) {
            return "${file.name} + is directory"
        }
        if (file.isFile) {
            return "${file.name} + is file"
        } else {
            return "${file.name} not file, not dir"
        }
    }

    fun checkFileExistance(file: File): Boolean {
        return file.exists()
    }
}
