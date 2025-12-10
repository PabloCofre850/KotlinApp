package com.example.kotlinapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.models.ItemReciclaje
import com.example.kotlinapp.vistas.HomeScreen
import com.example.kotlinapp.vistas.LoginScreen
import com.example.kotlinapp.vistas.RegisterUserScreen
import com.example.kotlinapp.vistas.ResultadoScreen
import com.example.kotlinapp.vistas.components.ErrorDialog

enum class Pantalla {
    LOGIN,
    REGISTRO,
    HOME,
    RESULTADOS
}

@Composable
fun App(
    modifierPadding: PaddingValues,
    photo: ImageBitmap?,
    geminiText: String,
    listaReciclaje: List<ItemReciclaje>,
    pantallaActual: Pantalla,
    onChangePantalla: (Pantalla) -> Unit,
    onOpenCamera: () -> Unit,
    onSendToGemini: () -> Unit,
    errorTitle: String?,
    errorMessage: String?,
    onDismissError: () -> Unit,
    clienteRepository: ClienteRepository // Inyectar repositorio
) {
    var username by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (pantallaActual) {
            Pantalla.LOGIN -> LoginScreen(
                irARegistro = { onChangePantalla(Pantalla.REGISTRO) },
                irAHome = { uname ->
                    username = uname // Guardamos el username
                    onChangePantalla(Pantalla.HOME)
                },
                clienteRepository = clienteRepository
            )

            Pantalla.REGISTRO -> RegisterUserScreen(
                irALogin = { onChangePantalla(Pantalla.LOGIN) },
                clienteRepository = clienteRepository
            )

            Pantalla.HOME -> HomeScreen(
                username = username, // Pasamos el username
                cerrarSesion = { onChangePantalla(Pantalla.LOGIN) },
                photo = photo,
                geminiText = geminiText,
                onOpenCamera = onOpenCamera,
                onSendToGemini = onSendToGemini
            )

            Pantalla.RESULTADOS -> ResultadoScreen(
                items = listaReciclaje,
                onTomarOtraFoto = { onChangePantalla(Pantalla.HOME) },
                onVolverHome = { onChangePantalla(Pantalla.HOME) }
            )
        }

        ErrorDialog(
            show = errorTitle != null && errorMessage != null,
            title = errorTitle ?: "Error",
            message = errorMessage ?: "Ha ocurrido un error inesperado.",
            onDismiss = onDismissError
        )
    }
}
