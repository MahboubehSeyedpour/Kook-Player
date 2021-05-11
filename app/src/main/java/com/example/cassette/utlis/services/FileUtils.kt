package com.example.cassette.utlis.services

import android.widget.Toast
import com.example.cassette.utlis.FilePathUtlis
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

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

    fun getFiles(file: File): Array<String> {
        val files: Array<String> = file.list()
        return files
    }
}
