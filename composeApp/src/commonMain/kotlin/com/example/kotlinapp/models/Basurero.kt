package com.example.kotlinapp.models

data class Basurero (
    private val color: String,
    private val description: String,
    private val esReciclable: Boolean
) {
    fun getColor():String = color
}