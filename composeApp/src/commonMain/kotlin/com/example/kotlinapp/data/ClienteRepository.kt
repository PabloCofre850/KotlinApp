package com.example.kotlinapp.data

import com.example.kotlinapp.models.Cliente

class ClienteRepository(
    private val persistencia: PersistenciaLocal
) {

    // Cargar los clientes desde el archivo al iniciar
    private val clientes = persistencia.cargar().map { serializable ->
        Cliente(
            username = serializable.username,
            nombres = serializable.nombres,
            apellidos = serializable.apellidos,
            email = serializable.email,
            ciudad = serializable.ciudad,
            region = serializable.region,
            pass = serializable.pass
        )
    }.toMutableList()

    fun registrarCliente(cliente: Cliente): Boolean {
        // Validar que el username o email no existan
        if (clientes.any { it.username == cliente.username || it.email == cliente.email }) {
            return false
        }
        clientes.add(cliente)
        guardarEnArchivo() // Guardar después de registrar
        return true
    }

    fun login(email: String, pass: String): Cliente? {
        return clientes.find { it.email == email && it.pass == pass }
    }

    fun obtenerTodos(): List<Cliente> = clientes.toList()

    private fun guardarEnArchivo() {
        val serializableList = clientes.map { cliente ->
            ClienteSerializable(
                username = cliente.username,
                nombres = cliente.nombres,
                apellidos = cliente.apellidos,
                email = cliente.email,
                ciudad = cliente.ciudad,
                region = cliente.region,
                pass = cliente.pass
            )
        }
        persistencia.guardar(serializableList)
    }
}
