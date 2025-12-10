package com.example.kotlinapp.vistas

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.vistas.components.BotonSecundario

@Composable
fun HomeScreen(
    nombreCliente: String,
    cerrarSesion: () -> Unit,
    photo: ImageBitmap?,
    geminiText: String,
    onOpenCamera: () -> Unit,
    onSendToGemini: () -> Unit
) {
    val clientes = emptyList<Any>() // temporal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text("Bienvenido, $nombreCliente", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        Text("Listado de cosas identificadas:", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(clientes) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Sin datos aún")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onOpenCamera,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar foto para identificar")
        }

        if (photo != null) {
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onSendToGemini,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar a Gemini")
            }
        }

        Spacer(Modifier.height(16.dp))

        BotonSecundario(
            texto = "Cerrar sesión",
            onClick = cerrarSesion,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
