package com.example.kotlinapp.models

class MaterialPlastico(
    nombre:String,
    puntosBase: Int = 50
) : Material(nombre, puntosBase){

    override fun obtenerContenedorAsignado(): Basurero {
        return Basurero("Amarillo", "Plasticos y Envases", true)
    }

    override fun calcularPuntosExtra(confianzaIA: Float): Int {
        return if (confianzaIA > 0.8f) (confianzaIA * 0.2).toInt() else 0
    }

}