package com.example.kotlinapp.data

import com.example.kotlinapp.models.Cliente

class ClienteRepository(
    private val persistencia: PersistenciaLocal
) {

    private val clientes = persistencia.cargar().map {
        Cliente(
            idCliente = it.idCliente,
            ciudad = it.ciudad,
            tieneDeuda = it.tieneDeuda,
            password = it.password,
            rut = it.rut,
            nombre = it.nombre,
            apellidoPaterno = it.apellidoPaterno,
            apellidoMaterno = it.apellidoMaterno,
            email = it.email
        )
    }.toMutableList()

    private var ultimoId = clientes.maxOfOrNull { it.idCliente } ?: 0

    fun registrarCliente(cliente: Cliente): Boolean {
        if (clientes.any { it.rut == cliente.rut || it.email == cliente.email }) {
            return false
        }

        ultimoId++
        val nuevo = cliente.copy(idCliente = ultimoId)
        clientes.add(nuevo)

        guardarEnArchivo()
        return true
    }

    fun login(email: String, password: String): Cliente? {
        return clientes.find { it.email == email && it.password == password }
    }

    fun obtenerTodos(): List<Cliente> = clientes.toList()

    private fun guardarEnArchivo() {
        val serializableList = clientes.map {
            ClienteSerializable(
                idCliente = it.idCliente,
                ciudad = it.ciudad,
                tieneDeuda = it.tieneDeuda,
                password = it.password,
                rut = it.rut,
                nombre = it.nombre,
                apellidoPaterno = it.apellidoPaterno,
                apellidoMaterno = it.apellidoMaterno,
                email = it.email
            )
        }
        persistencia.guardar(serializableList)
    }
}
