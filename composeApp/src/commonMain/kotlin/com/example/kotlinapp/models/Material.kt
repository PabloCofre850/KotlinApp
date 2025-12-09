package com.example.kotlinapp.models

abstract class Material (
    private val nombreMaterial: String,
    private val puntosBase: Int
){
    fun getNombre(): String = nombreMaterial
    fun getPuntosBase(): Int = puntosBase

    abstract fun obtenerContenedorAsignado(): Basurero

    open fun calcularPuntosExtra(confianzaIA: Float): Int {
        return if (confianzaIA > 0.9f) (puntosBase * 0.05).toInt() else 0
    }
}