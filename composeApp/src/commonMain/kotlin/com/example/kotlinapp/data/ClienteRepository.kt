package com.example.kotlinapp.data

import com.example.kotlinapp.models.Cliente

/**
 * Repositorio en memoria para manejar clientes.
 * La persistencia se ha omitido temporalmente para simplificar.
 */
class ClienteRepository {

    private val clientes = mutableListOf<Cliente>()

    fun registrarCliente(cliente: Cliente): Boolean {
        // Validar que el username o email no existan
        if (clientes.any { it.username == cliente.username || it.email == cliente.email }) {
            return false
        }
        clientes.add(cliente)
        println("Cliente registrado: ${cliente.username}")
        return true
    }

    fun login(email: String, pass: String): Cliente? {
        val cliente = clientes.find { it.email == email && it.pass == pass }
        println("Intento de login para $email: ${if (cliente != null) "Exitoso" else "Fallido"}")
        return cliente
    }

    fun obtenerTodos(): List<Cliente> = clientes.toList()
}
