package com.example.kotlinapp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import com.example.kotlinapp.vistas.HomeScreen
import com.example.kotlinapp.vistas.LoginScreen
import com.example.kotlinapp.vistas.RegisterUserScreen


// Pantallas disponibles (nuestro "NavController simple")
enum class Pantalla {
    LOGIN,
    REGISTRO,
    HOME
}

@Composable
fun App() {
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
                    }
                )

                Pantalla.REGISTRO -> RegisterUserScreen(
                    irALogin = { pantallaActual = Pantalla.LOGIN }
                )

                Pantalla.HOME -> HomeScreen(
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