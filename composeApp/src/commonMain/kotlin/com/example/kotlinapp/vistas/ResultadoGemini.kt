package com.example.kotlinapp.models

import kotlinx.serialization.Serializable

@Serializable
data class ResultadoGemini(
    val items: List<ItemReciclaje>
)

