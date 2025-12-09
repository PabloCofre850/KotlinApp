package com.example.kotlinapp.data

import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class PersistenciaLocal(private val storage: FileStorage) {

    private val json = Json { prettyPrint = true }

    fun guardar(lista: List<ClienteSerializable>) {
        val contenido = json.encodeToString(lista)
        storage.writeText("clientes.json", contenido)
    }

    fun cargar(): List<ClienteSerializable> {
        val contenido = storage.readText("clientes.json") ?: return emptyList()
        return json.decodeFromString(contenido)
    }
}
