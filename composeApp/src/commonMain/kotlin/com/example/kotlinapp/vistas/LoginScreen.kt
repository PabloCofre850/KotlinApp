package com.example.kotlinapp.vistas

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.vistas.components.BotonLink
import com.example.kotlinapp.vistas.components.BotonPrincipal

@Composable
fun LoginScreen(
    irARegistro: () -> Unit,
    irAHome: (String) -> Unit  // pasamos el nombre del cliente
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Proceso de Reciclaje" + "\n" + "Inicio de sesion", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        BotonPrincipal(
            texto = "Iniciar sesión",
            onClick = {
                val cliente = ClienteRepository.login(email.trim(), password.trim())
                if (cliente != null) {
                    mensajeError = null
                    irAHome(cliente.nombre)
                } else {
                    mensajeError = "Correo o contraseña incorrectos"
                }
            }
        )

        mensajeError?.let {
            Spacer(Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        BotonLink(
            texto = "¿No tienes cuenta? Regístrate aquí",
            onClick = irARegistro
        )
    }
}