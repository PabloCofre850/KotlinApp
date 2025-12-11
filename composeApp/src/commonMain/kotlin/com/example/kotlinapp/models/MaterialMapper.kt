package com.example.kotlinapp.models

fun mapearMaterial(materialIA: String): Material {
    val m = materialIA.lowercase()

    return when {
        "plast" in m -> MaterialPlastico()
        "pet" in m -> MaterialPlastico()
        "papel" in m -> MaterialPapel()
        "cart" in m -> MaterialCarton()
        "orga" in m -> MaterialOrganico()
        else -> MaterialDesconocido()
    }
}
