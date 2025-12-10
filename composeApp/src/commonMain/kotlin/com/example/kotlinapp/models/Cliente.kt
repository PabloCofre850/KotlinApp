package com.example.kotlinapp.models

class Cliente(
    username: String,
    nombres: String,
    apellidos: String,
    email: String,
    ciudad: String,
    region: String,
    var pass: String
) : Persona(username, nombres, apellidos, email, ciudad, region)
