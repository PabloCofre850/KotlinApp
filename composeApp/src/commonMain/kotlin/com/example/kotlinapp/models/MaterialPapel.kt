package com.example.kotlinapp.models

class MaterialPapel : Material() {
    override val nombre = "Papel"

    override fun obtenerColorBasurero() = "Azul"

    override fun obtenerInstrucciones() =
        "1. Asegúrese de que el papel esté limpio.\n2. Retire adhesivos o plásticos.\n3. Deposite en el contenedor Azul."
}
