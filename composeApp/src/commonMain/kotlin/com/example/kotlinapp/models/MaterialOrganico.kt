package com.example.kotlinapp.models

class MaterialOrganico : Material() {
    override val nombre = "Orgánico"

    override fun obtenerColorBasurero() = "Marrón"

    override fun obtenerInstrucciones() =
        "1. Separe restos de comida y vegetales.\n2. Evite mezclar con plásticos.\n3. Deposite en el contenedor Marrón."
}
