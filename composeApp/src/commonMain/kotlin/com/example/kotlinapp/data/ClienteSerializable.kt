package com.example.kotlinapp.data

import kotlinx.serialization.Serializable

@Serializable
data class ClienteSerializable(
    val idCliente: Int,
    val ciudad: String,
    val tieneDeuda: Boolean,
    val password: String,
    val rut: String,
    val nombre: String,
    val apellidoPaterno: String,
    val apellidoMaterno: String,
    val email: String
)
