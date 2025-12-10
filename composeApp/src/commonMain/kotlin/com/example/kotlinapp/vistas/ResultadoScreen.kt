package com.example.kotlinapp.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
            .padding(24.dp)
    ) {

        Text(
            "Centro de Reciclaje",
            style = MaterialTheme.typography.headlineMedium,
            color = RecycleGreenDark
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = RecycleGreenSurface // Fondo sutil para las tarjetas
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Nombre: ${item.nombre}", color = RecycleGreenDark)
                        Text("Material: ${item.material}", color = RecycleGreenDark)
                        Text("Basurero: ${item.colorBasurero}", color = RecycleGreenPrimary)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        BotonPrincipal(
            texto = "Tomar otra foto",
            onClick = onTomarOtraFoto
        )

        Spacer(Modifier.height(8.dp))

        BotonSecundario(
            texto = "Volver al inicio",
            onClick = onVolverHome
        )
    }
}
