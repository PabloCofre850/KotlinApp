package com.example.kotlinapp.vistas.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.ui.theme.RecycleGreenDark
import com.example.kotlinapp.ui.theme.RecycleGreenPrimary
import com.example.kotlinapp.ui.theme.RecycleWhite

/**
 * Botón principal reutilizable para toda la app.
 */
@Composable
fun BotonPrincipal(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = RecycleGreenPrimary, // Verde principal
            contentColor = RecycleWhite // Texto blanco
        )
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

/**
 * Botón secundario (outline) para acciones complementarias.
 */
@Composable
fun BotonSecundario(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = RecycleGreenPrimary // Texto del color principal
        ),
        border = BorderStroke(1.dp, RecycleGreenPrimary) // Borde del color principal
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Botón tipo enlace (texto clickeable).
 */
@Composable
fun BotonLink(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyMedium,
            color = RecycleGreenDark // Verde oscuro para el enlace
        )
    }
}
