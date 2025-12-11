package com.example.kotlinapp.models

abstract class Material {
    abstract val nombre: String
    abstract fun obtenerColorBasurero(): String
    abstract fun obtenerInstrucciones(): String
}
