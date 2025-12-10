package com.example.kotlinapp.models

abstract class Persona(
    val username: String,
    val nombres: String,
    val apellidos: String,
    val email: String,
    var ciudad: String,
    var region: String
)
