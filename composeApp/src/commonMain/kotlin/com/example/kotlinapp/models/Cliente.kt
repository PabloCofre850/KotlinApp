package com.example.kotlinapp.models

data class Cliente(
    val idCliente: Int,
    val direccion: String,
    val ciudad: String,
    val tieneDeuda: Boolean,
    val password: String,
    // Atributos heredados desde Persona
    override val rut: String,
    override val nombre: String,
    override val apellidoPaterno: String,
    override val apellidoMaterno: String,
    override val telefono: String,
    override val email: String
) : Persona(
    rut = rut,
    nombre = nombre,
    apellidoPaterno = apellidoPaterno,
    apellidoMaterno = apellidoMaterno,
    telefono = telefono,
    email = email
)