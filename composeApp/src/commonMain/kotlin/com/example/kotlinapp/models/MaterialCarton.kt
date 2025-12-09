package com.example.kotlinapp.models

class MaterialCarton(
    nombre:String,
    puntosBase: Int = 40
) : Material(nombre, puntosBase){

    override fun obtenerContenedorAsignado(): Basurero {
        return Basurero("Azul", "Carton corrugado", true)
    }

}