package com.example.kotlinapp.models

class MaterialDesconocido : Material() {
    override val nombre = "Desconocido"

    override fun obtenerColorBasurero() = "Gris"

    override fun obtenerInstrucciones() =
        "No se pudo determinar el material. Deposite en el contenedor Gris o trate de clasificar manualmente."
}
