package com.example.kotlinapp.data

expect class FileStorage {
    fun writeText(filename: String, text: String)
    fun readText(filename: String): String?
}
