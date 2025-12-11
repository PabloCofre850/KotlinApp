package com.example.kotlinapp.models

class MaterialCarton : Material() {
    override val nombre = "Cartón"

    override fun obtenerColorBasurero() = "Azul"

    override fun obtenerInstrucciones() =
        "1. Desarme y aplaste las cajas.\n2. Mantenga el cartón seco y sin restos de comida.\n3. Deposite en el contenedor Azul."
}
