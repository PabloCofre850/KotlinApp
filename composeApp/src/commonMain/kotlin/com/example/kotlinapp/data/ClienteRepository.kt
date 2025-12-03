package com.example.kotlinapp.data

import com.example.kotlinapp.models.Cliente

/**
 * Repositorio en memoria.
 * Guarda los clientes en una lista mutable.
 */
object ClienteRepository {

    private val clientes = mutableListOf<Cliente>()
    private var ultimoId = 0

    fun registrarCliente(cliente: Cliente): Boolean {
        // No permitir dos clientes con el mismo RUT o correo
        if (clientes.any { it.rut == cliente.rut || it.email == cliente.email }) {
            return false
        }
        ultimoId++
        val nuevoCliente = cliente.copy(idCliente = ultimoId)
        clientes.add(nuevoCliente)
        return true
    }

    /**
     * Valida un inicio de sesión por email y password.
     */
    fun login(email: String, password: String): Cliente? {
        return clientes.find { it.email == email && it.password == password }
    }

    fun obtenerTodos(): List<Cliente> = clientes.toList()
}