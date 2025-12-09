package com.example.kotlinapp.vistas

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.vistas.components.BotonSecundario


@Composable
fun HomeScreen(
    repo: ClienteRepository,
    nombreCliente: String,
    cerrarSesion: () -> Unit,
    photo: ImageBitmap?,
    geminiText: String,
    onOpenCamera: () -> Unit,
    onSendToGemini: () -> Unit
) {
    val clientes = repo.obtenerTodos()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(
            text = "Bienvenido, $nombreCliente",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Listado de cosas identificadas:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(clientes) { c ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${c.nombre} ${c.apellidoPaterno} (${c.rut})")
                        Text("Material: ${c.email}")
                        Text("Color de basurero: ${c.ciudad}")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // -------- BOTÓN CÁMARA --------
        Button(
            onClick = onOpenCamera,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tomar foto para identificar")
        }

        // -------- NO MOSTRAMOS LA IMAGEN, SOLO EL BOTÓN PARA ENVIAR --------
        if (photo != null) {
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onSendToGemini,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar a Gemini")
            }
        }

        // -------- MOSTRAR RESPUESTA DE GEMINI --------
        if (geminiText.isNotBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = geminiText,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(16.dp))

        BotonSecundario(
            texto = "Cerrar sesión",
            onClick = cerrarSesion,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
