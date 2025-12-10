package com.example.kotlinapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
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
    // ELIMINADO: onSendToGemini
    errorTitle: String?,
    errorMessage: String?,
    onDismissError: () -> Unit
) {

    var nombreCliente by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {

        when (pantallaActual) {

            Pantalla.LOGIN -> LoginScreen(
                irARegistro = { onChangePantalla(Pantalla.REGISTRO) },
                irAHome = { nombre ->
                    nombreCliente = nombre
                    onChangePantalla(Pantalla.HOME)
                }
            )

            Pantalla.REGISTRO -> RegisterUserScreen(
                irALogin = { onChangePantalla(Pantalla.LOGIN) }
            )

            Pantalla.HOME -> HomeScreen(
                nombreCliente = nombreCliente,
                cerrarSesion = { onChangePantalla(Pantalla.LOGIN) },
                photo = photo,
                geminiText = geminiText,
                onOpenCamera = onOpenCamera
                // REMOVIDO: onSendToGemini
            )

            Pantalla.RESULTADOS -> ResultadoScreen(
                items = listaReciclaje,
                onTomarOtraFoto = { onChangePantalla(Pantalla.HOME) },
                onVolverHome = { onChangePantalla(Pantalla.HOME) }
            )
        }

        // --- Cuadro de error global ---
        ErrorDialog(
            show = errorTitle != null && errorMessage != null,
            title = errorTitle ?: "Error",
            message = errorMessage ?: "Ha ocurrido un error inesperado.",
            onDismiss = onDismissError
        )
    }
}
