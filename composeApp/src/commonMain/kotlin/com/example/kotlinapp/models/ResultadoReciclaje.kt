package com.example.kotlinapp.vistas

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.material3.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class ResultadoReciclaje(
    val organico: List<String>,
    val plastico: List<String>,
    val papel: List<String>,
    val carton: List<String>
)

@Composable
fun ResultadoScreen(
    geminiResponse: String,
    irAHome: () -> Unit
) {
    val json = Json { ignoreUnknownKeys = true }

    val datos = try {
        json.decodeFromString<ResultadoReciclaje>(geminiResponse)
    } catch (e: Exception) {
        null
    }

    Column {
        Text("Resultados del análisis")

        datos?.let {
            Text("Orgánico: ${it.organico}")
            Text("Plástico: ${it.plastico}")
            Text("Papel: ${it.papel}")
            Text("Cartón: ${it.carton}")
        }

        Button(onClick = irAHome) {
            Text("Volver")
        }
    }
}
