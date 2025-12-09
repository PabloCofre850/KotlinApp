package com.example.kotlinapp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import com.example.kotlinapp.vistas.HomeScreen
import com.example.kotlinapp.vistas.LoginScreen
import com.example.kotlinapp.vistas.RegisterUserScreen
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.data.FileStorage
import com.example.kotlinapp.data.PersistenciaLocal

// Pantallas disponibles (nuestro "NavController simple")
enum class Pantalla {
    LOGIN,
    REGISTRO,
    HOME
}

@Composable
fun App(
    storage: FileStorage,
    modifierPadding: PaddingValues = PaddingValues(0.dp),
    photo: ImageBitmap? = null,
    geminiText: String = "",
    onOpenCamera: () -> Unit = {},
    onSendToGemini: () -> Unit = {},
) {

    val repo = remember {
        ClienteRepository(PersistenciaLocal(storage))
    }

    var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }
    var nombreClienteActual by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

            when (pantallaActual) {

                Pantalla.LOGIN -> LoginScreen(
                    repo = repo,
                    irARegistro = { pantallaActual = Pantalla.REGISTRO },
                    irAHome = { nombre ->
                        nombreClienteActual = nombre
                        pantallaActual = Pantalla.HOME
                    }
                )

                Pantalla.REGISTRO -> RegisterUserScreen(
                    repo = repo,
                    irALogin = { pantallaActual = Pantalla.LOGIN }
                )

                Pantalla.HOME -> HomeScreen(
                    repo = repo,
                    nombreCliente = nombreClienteActual,
                    cerrarSesion = {
                        nombreClienteActual = ""
                        pantallaActual = Pantalla.LOGIN
                    },
                    photo = photo,
                    geminiText = geminiText,
                    onOpenCamera = onOpenCamera,
                    onSendToGemini = onSendToGemini
                )
            }
        }
    }
}
