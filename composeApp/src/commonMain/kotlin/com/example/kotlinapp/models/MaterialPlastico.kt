package com.example.kotlinapp.models

class MaterialPlastico : Material() {
    override val nombre = "Plástico"

    override fun obtenerColorBasurero() = "Amarillo"

    override fun obtenerInstrucciones() =
        "1. Lave y seque el envase.\n2. Aplaste para reducir el volumen.\n3. Deposite en el contenedor Amarillo."
}
