package com.example.kotlinapp.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

// Datos para la guía de colores
data class InfoBasurero(
    val color: Color,
    val nombre: String,
    val emoji: String,
    val descripcion: String,
    val textColor: Color = Color.White // Por defecto texto blanco para contraste
)

@Composable
fun HomeScreen(
    username: String,
    cerrarSesion: () -> Unit,
    photo: ImageBitmap?,
    geminiText: String,
    onOpenCamera: () -> Unit,
    onSendToGemini: () -> Unit
) {
    val consejos = listOf(
        "Separa siempre el plástico, vidrio y papel.",
        "Lava los envases antes de reciclarlos.",
        "Las tapas de plástico también se reciclan.",
        "Reutiliza las bolsas de tela para tus compras."
    )

    val guiaBasureros = listOf(
        InfoBasurero(Color(0xFF1976D2), "Papel y Cartón", "📄", "Cajas, hojas, diarios, revistas limpias."), // Azul
        InfoBasurero(Color(0xFFFFEB3B), "Plásticos", "🥤", "Botellas PET, envases limpios.", textColor = Color.Black), // Amarillo
        InfoBasurero(Color(0xFF388E3C), "Vidrio", "🍾", "Botellas, frascos (sin tapas)."), // Verde
        InfoBasurero(Color(0xFF795548), "Orgánicos", "🍎", "Cáscaras, restos de fruta y verdura."), // Marrón
        InfoBasurero(Color(0xFF616161), "Restos", "🗑️", "Lo que no se puede reciclar."), // Gris
        InfoBasurero(Color(0xFFD32F2F), "Peligrosos", "☣️", "Pilas, baterías, aceites.") // Rojo
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecycleWhite)
            .verticalScroll(rememberScrollState()) // Permitir scroll vertical
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // --- Saludo ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "¡Hola, $username!",
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
                Text("📸", fontSize = 60.sp)
                Spacer(Modifier.height(12.dp))
                Text("¿Listo para reciclar?", style = MaterialTheme.typography.titleLarge, color = RecycleGreenDark)
                Spacer(Modifier.height(4.dp))
                Text("Toma una foto de un objeto para saber cómo reciclarlo.", style = MaterialTheme.typography.bodyMedium, color = RecycleGreenDark)
                Spacer(Modifier.height(16.dp))
                BotonPrincipal(
                    texto = "Escanear Producto",
                    onClick = onOpenCamera
                )
            }
        }

        // --- Sección Guía de Colores ---
        Spacer(Modifier.height(32.dp))
        Text("Guía de Colores", style = MaterialTheme.typography.titleLarge, color = RecycleGreenDark, modifier = Modifier.padding(bottom = 12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(guiaBasureros) { info ->
                BasureroCard(info)
            }
        }

        // --- Sección de Consejos ---
        Spacer(Modifier.height(32.dp))
        Text("Consejos Rápidos", style = MaterialTheme.typography.titleLarge, color = RecycleGreenDark, modifier = Modifier.padding(bottom = 12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(consejos) { consejo ->
                ConsejoCard(consejo)
            }
        }

        Spacer(Modifier.height(32.dp))

        BotonSecundario(
            texto = "Cerrar sesión",
            onClick = cerrarSesion
        )
        
        // Espacio final para scroll
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun BasureroCard(info: InfoBasurero) {
    Card(
        modifier = Modifier.width(200.dp).height(200.dp), // Aumentado significativamente
        colors = CardDefaults.cardColors(containerColor = info.color),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(info.emoji, fontSize = 40.sp) // Emoji más grande
            
            Column {
                Text(
                    text = info.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = info.textColor
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = info.descripcion,
                    style = MaterialTheme.typography.bodyMedium, // Aumentado a bodyMedium
                    color = info.textColor.copy(alpha = 0.95f),
                    lineHeight = 20.sp // Mayor interlineado
                )
            }
        }
    }
}

@Composable
private fun ConsejoCard(texto: String) {
    Card(
        modifier = Modifier.width(180.dp).height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text("💡", fontSize = 24.sp)
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
