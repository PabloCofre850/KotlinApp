package com.example.kotlinapp.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.models.ItemReciclaje
import com.example.kotlinapp.ui.theme.*
import com.example.kotlinapp.vistas.components.BotonPrincipal
import com.example.kotlinapp.vistas.components.BotonSecundario

@Composable
fun ResultadoScreen(
    items: List<ItemReciclaje>,
    onVolverHome: () -> Unit,
    onTomarOtraFoto: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecycleWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // --- Título ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "¡Resultado!",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = RecycleGreenDark
            )
            Spacer(Modifier.width(8.dp))
            Text("✅", fontSize = 32.sp)
        }
        Text(
            text = "Esto es lo que encontramos:",
            style = MaterialTheme.typography.bodyLarge,
            color = RecycleGreenDark,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- Lista de Resultados ---
        if (items.isEmpty()) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No se encontraron objetos reciclables en la imagen.",
                    style = MaterialTheme.typography.titleMedium,
                    color = RecycleGreenMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items) { item ->
                    ResultadoCard(item)
                }
            }
        }

        // --- Botones de Acción ---
        Spacer(Modifier.height(24.dp))
        BotonPrincipal(
            texto = "Escanear otro objeto",
            onClick = onTomarOtraFoto
        )
        Spacer(Modifier.height(8.dp))
        BotonSecundario(
            texto = "Volver al inicio",
            onClick = onVolverHome
        )
    }
}

@Composable
private fun ResultadoCard(item: ItemReciclaje) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = RecycleGreenSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.nombre,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = RecycleGreenDark
            )
            Spacer(Modifier.height(8.dp))
            InfoLinea(emoji = "🧱", titulo = "Material:", valor = item.material)
            InfoLinea(emoji = "🗑️", titulo = "Basurero:", valor = item.colorBasurero)
        }
    }
}

@Composable
private fun InfoLinea(emoji: String, titulo: String, valor: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 16.sp)
        Spacer(Modifier.width(8.dp))
        Text(
            text = "$titulo ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = RecycleGreenDark
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = RecycleGreenPrimary
        )
    }
}
