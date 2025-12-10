package com.example.kotlinapp

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.models.ItemReciclaje
import com.example.kotlinapp.vistas.HomeScreen
import com.example.kotlinapp.vistas.LoginScreen
import com.example.kotlinapp.vistas.RegisterUserScreen
import com.example.kotlinapp.vistas.ResultadoScreen

// Pantallas disponibles (nuestro "NavController simple")
enum class Pantalla {
    LOGIN,
    REGISTRO,
    HOME,
    RESULTADOS
}

@Composable
fun App(
    modifierPadding: PaddingValues = PaddingValues(0.dp),
    photo: ImageBitmap?,
    geminiText: String,
    listaReciclaje: List<ItemReciclaje>,
    pantallaActual: Pantalla,
    onChangePantalla: (Pantalla) -> Unit,
    onOpenCamera: () -> Unit,
    onSendToGemini: () -> Unit,
) {
    var nombreClienteActual by remember { mutableStateOf("") }

    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(modifierPadding)
        ) {
            when (pantallaActual) {

                Pantalla.LOGIN -> LoginScreen(
                    irARegistro = { onChangePantalla(Pantalla.REGISTRO) },
                    irAHome = { nombre ->
                        nombreClienteActual = nombre
                        onChangePantalla(Pantalla.HOME)
                    }
                )

                Pantalla.REGISTRO -> RegisterUserScreen(
                    irALogin = { onChangePantalla(Pantalla.LOGIN) }
                )

                Pantalla.HOME -> HomeScreen(
                    nombreCliente = nombreClienteActual,
                    cerrarSesion = {
                        nombreClienteActual = ""
                        onChangePantalla(Pantalla.LOGIN)
                    },
                    photo = photo,
                    geminiText = geminiText,
                    onOpenCamera = onOpenCamera,
                    onSendToGemini = onSendToGemini
                )

                Pantalla.RESULTADOS -> ResultadoScreen(
                    items = listaReciclaje,
                    onTomarOtraFoto = {
                        onChangePantalla(Pantalla.HOME)
                    },
                    onVolverHome = {
                        onChangePantalla(Pantalla.HOME)
                    }
                )
            }
        }
    }
}
