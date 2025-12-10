package com.example.kotlinapp.models

import kotlinx.serialization.Serializable

@Serializable
data class ItemReciclaje(
    val nombre: String,
    val material: String,
    val colorBasurero: String
)
