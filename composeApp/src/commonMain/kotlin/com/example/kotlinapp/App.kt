package com.example.kotlinapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.models.ItemReciclaje
import com.example.kotlinapp.ui.theme.RecycleGreenPrimary
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
    clienteRepository: ClienteRepository,
    isLoading: Boolean // Recibir estado de carga
) {
    var username by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (pantallaActual) {
                Pantalla.LOGIN -> LoginScreen(
                    irARegistro = { onChangePantalla(Pantalla.REGISTRO) },
                    irAHome = { uname ->
                        username = uname
                        onChangePantalla(Pantalla.HOME)
                    },
                    clienteRepository = clienteRepository
                )

                Pantalla.REGISTRO -> RegisterUserScreen(
                    irALogin = { onChangePantalla(Pantalla.LOGIN) },
                    clienteRepository = clienteRepository
                )

                Pantalla.HOME -> HomeScreen(
                    username = username,
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

        // --- Overlay de Carga ---
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = RecycleGreenPrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Analizando imagen...", color = Color.White)
                }
            }
        }
    }
}
