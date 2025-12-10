package com.example.kotlinapp.models

@kotlinx.serialization.Serializable
data class ItemReciclaje (
    val nombre: String,
    val material: String,
    val colorBasurero: String
)