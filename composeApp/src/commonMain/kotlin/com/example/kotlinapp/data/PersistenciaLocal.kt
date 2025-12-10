package com.example.kotlinapp.data

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PersistenciaLocal(private val storage: FileStorage) {

    // Configuración de JSON
    private val json = Json { 
        prettyPrint = true 
        ignoreUnknownKeys = true // Ignorar campos que no existen en el modelo actual
        isLenient = true // Ser más flexible
    }

    fun guardar(lista: List<ClienteSerializable>) {
        try {
            val contenido = json.encodeToString(lista)
            storage.writeText("clientes.json", contenido)
        } catch (e: Exception) {
            println("Error al guardar clientes: ${e.message}")
        }
    }

    fun cargar(): List<ClienteSerializable> {
        val contenido = storage.readText("clientes.json") ?: return emptyList()
        return try {
            json.decodeFromString(contenido)
        } catch (e: Exception) {
            println("Error al cargar clientes (formato incompatible o corrupto): ${e.message}")
            // Si falla la lectura
            emptyList()
        }
    }
}
