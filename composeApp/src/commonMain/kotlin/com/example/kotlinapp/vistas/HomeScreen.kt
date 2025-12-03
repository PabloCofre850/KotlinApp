package com.example.kotlinapp.vistas

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.vistas.components.BotonSecundario


@Composable
fun HomeScreen(
    nombreCliente: String,
    cerrarSesion: () -> Unit
) {
    val clientes = ClienteRepository.obtenerTodos()

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
            text = "Listado de clientes registrados:",
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
                        Text("Correo: ${c.email}")
                        Text("Teléfono: ${c.telefono}")
                        Text("Ciudad: ${c.ciudad}")
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BotonSecundario(
                texto = "Cerrar sesión",
                onClick = cerrarSesion
            )
        }
    }
}