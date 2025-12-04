package com.example.kotlinapp.models

data class Cliente(
    val idCliente: Int,
    val ciudad: String,
    val tieneDeuda: Boolean,
    val password: String,
    // Atributos heredados desde Persona
    override val rut: String,
    override val nombre: String,
    override val apellidoPaterno: String,
    override val apellidoMaterno: String,
    override val email: String
) : Persona(
    rut = rut,
    nombre = nombre,
    apellidoPaterno = apellidoPaterno,
    apellidoMaterno = apellidoMaterno,
    email = email
)