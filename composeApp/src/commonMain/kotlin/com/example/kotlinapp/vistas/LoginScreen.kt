package com.example.kotlinapp.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.ui.theme.*
import com.example.kotlinapp.vistas.components.BotonLink
import com.example.kotlinapp.vistas.components.BotonPrincipal

@Composable
fun LoginScreen(
    irARegistro: () -> Unit,
    irAHome: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecycleWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        // Título con Emoji
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Logueate!",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = RecycleGreenPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "🌱",
                fontSize = 40.sp 
            )
        }
        
        Text(
            text = "Ingresa a tu cuenta",
            style = MaterialTheme.typography.bodyLarge,
            color = RecycleGreenDark,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RecycleGreenPrimary,
                unfocusedBorderColor = RecycleGreenMedium,
                cursorColor = RecycleGreenPrimary,
                focusedLabelColor = RecycleGreenPrimary,
                unfocusedLabelColor = RecycleGreenMedium,
                focusedContainerColor = RecycleWhite,
                unfocusedContainerColor = RecycleWhite
            )
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RecycleGreenPrimary,
                unfocusedBorderColor = RecycleGreenMedium,
                cursorColor = RecycleGreenPrimary,
                focusedLabelColor = RecycleGreenPrimary,
                unfocusedLabelColor = RecycleGreenMedium,
                focusedContainerColor = RecycleWhite,
                unfocusedContainerColor = RecycleWhite
            )
        )

        Spacer(Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Iniciar sesión",
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    mensajeError = null
                    irAHome(email.substringBefore("@"))
                } else {
                    mensajeError = "Correo o contraseña incorrectos"
                }
            }
        )

        mensajeError?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = ErrorRed)
        }

        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            BotonLink(
                texto = "¿No tienes cuenta? Regístrate aquí",
                onClick = irARegistro
            )
        }
    }
}
