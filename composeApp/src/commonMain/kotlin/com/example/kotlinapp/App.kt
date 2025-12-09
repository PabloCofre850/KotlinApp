package com.example.kotlinapp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.data.FileStorage
import com.example.kotlinapp.data.PersistenciaLocal
import com.example.kotlinapp.vistas.HomeScreen
import com.example.kotlinapp.vistas.LoginScreen
import com.example.kotlinapp.vistas.RegisterUserScreen

// Pantallas disponibles
enum class Pantalla {
    LOGIN,
    REGISTRO,
    HOME
}

@Composable
fun App(storage: FileStorage) {

    // Crear repositorio con persistencia real
    val repo = remember {
        ClienteRepository(
            PersistenciaLocal(storage)
        )
    }

    var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }
    var nombreClienteActual by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

            when (pantallaActual) {

                Pantalla.LOGIN -> LoginScreen(
                    irARegistro = { pantallaActual = Pantalla.REGISTRO },
                    irAHome = { nombre ->
                        nombreClienteActual = nombre
                        pantallaActual = Pantalla.HOME
                    },
                    repo = repo
                )

                Pantalla.REGISTRO -> RegisterUserScreen(
                    irALogin = { pantallaActual = Pantalla.LOGIN },
                    repo = repo
                )

                Pantalla.HOME -> HomeScreen(
                    repo = repo,
                    nombreCliente = nombreClienteActual,
                    cerrarSesion = {
                        nombreClienteActual = ""
                        pantallaActual = Pantalla.LOGIN
                    }
                )
            }
        }
    }
}
