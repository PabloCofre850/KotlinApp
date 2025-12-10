package com.example.kotlinapp.data

import kotlinx.serialization.Serializable

@Serializable
data class ClienteSerializable(
    val username: String,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val ciudad: String,
    val region: String,
    val pass: String
)
