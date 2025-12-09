package com.example.kotlinapp.models

class MaterialOrganico(
    nombre: String,
    puntosBase: Int = 0
) : Material(nombre, puntosBase){

    override fun obtenerContenedorAsignado(): Basurero {
        return Basurero("Cafe", "Residuos organicos y generales", false)
    }

    override fun calcularPuntosExtra(confianzaIA: Float): Int {
        return 0
    }

}