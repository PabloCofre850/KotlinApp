package com.example.kotlinapp.data

import android.content.Context
import java.io.File

actual class FileStorage(private val context: Context) {

    actual fun writeText(filename: String, text: String) {
        val file = File(context.filesDir, filename)
        file.writeText(text)
    }

    actual fun readText(filename: String): String? {
        val file = File(context.filesDir, filename)
        return if (file.exists()) file.readText() else null
    }
}
