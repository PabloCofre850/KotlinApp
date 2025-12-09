package com.example.kotlinapp.models

class MaterialPapel(
    nombre: String,
    private val estaLimpio: Boolean,
    puntosBase: Int = 30
) : Material(nombre, puntosBase){

    override fun obtenerContenedorAsignado(): Basurero {
        return if (estaLimpio){
            Basurero("Azul", "Papel y Carton", true)
        } else {
            Basurero("Organico", "Residuos no reciclables (estan sucios)", false)
        }
    }
}