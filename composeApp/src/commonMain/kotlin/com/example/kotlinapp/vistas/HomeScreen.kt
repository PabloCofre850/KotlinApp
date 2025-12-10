package com.example.kotlinapp.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.ui.theme.*
import com.example.kotlinapp.vistas.components.BotonPrincipal
import com.example.kotlinapp.vistas.components.BotonSecundario

@Composable
fun HomeScreen(
    nombreCliente: String,
    cerrarSesion: () -> Unit,
    photo: ImageBitmap?,
    geminiText: String,
    onOpenCamera: () -> Unit,   // ← SOLO ESTO SE USA
) {
    val consejos = listOf(
        "Separa siempre el plástico, vidrio y papel.",
        "Lava los envases antes de reciclarlos.",
        "Las tapas de plástico también se reciclan.",
        "Reutiliza las bolsas de tela para tus compras."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecycleWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // --- Saludo ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "¡Hola, $nombreCliente!",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = RecycleGreenDark
            )
            Spacer(Modifier.width(8.dp))
            Text("👋", fontSize = 32.sp)
        }

        Spacer(Modifier.height(32.dp))

        // --- Tarjeta Principal de Acción ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = RecycleGreenSurface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "📸", fontSize = 60.sp)

                Spacer(Modifier.height(12.dp))

                Text(
                    "¿Listo para reciclar?",
                    style = MaterialTheme.typography.titleLarge,
                    color = RecycleGreenDark
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Toma una foto de un objeto para saber cómo reciclarlo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = RecycleGreenDark
                )

                Spacer(Modifier.height(16.dp))

                // *** BOTÓN CORRECTO ***
                BotonPrincipal(
                    texto = "Escanear Producto",
                    onClick = onOpenCamera   // ← FLUJO AUTOMÁTICO
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            "Consejos Rápidos",
            style = MaterialTheme.typography.titleLarge,
            color = RecycleGreenDark,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(consejos) { consejo ->
                ConsejoCard(consejo)
            }
        }

        Spacer(Modifier.weight(1f))

        BotonSecundario(
            texto = "Cerrar sesión",
            onClick = cerrarSesion
        )
    }
}

@Composable
private fun ConsejoCard(texto: String) {
    Card(
        modifier = Modifier.width(180.dp).height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(text = "💡", fontSize = 24.sp)

            Spacer(Modifier.height(8.dp))

            Text(
                text = texto,
                style = MaterialTheme.typography.bodySmall,
                color = RecycleGreenDark,
                lineHeight = 18.sp
            )
        }
    }
}
