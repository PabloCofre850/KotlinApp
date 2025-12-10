package com.example.kotlinapp.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.ui.theme.*
import com.example.kotlinapp.vistas.components.BotonLink
import com.example.kotlinapp.vistas.components.BotonPrincipal

@Composable
fun LoginScreen(
    irARegistro: () -> Unit,
    irAHome: (String) -> Unit, // Recibe el username
    clienteRepository: ClienteRepository
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    fun realizarLogin() {
        val cliente = clienteRepository.login(email, password)
        if (cliente != null) {
            mensajeError = null
            irAHome(cliente.username) // Pasa el username
        } else {
            mensajeError = "Correo o contraseña incorrectos"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecycleWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        // --- Título ---
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {
            Text("Logueate!", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold), color = RecycleGreenPrimary)
            Spacer(Modifier.width(8.dp))
            Text("🌱", fontSize = 40.sp)
        }
        Text("Ingresa a tu cuenta de reciclaje", style = MaterialTheme.typography.bodyLarge, color = RecycleGreenDark, modifier = Modifier.padding(bottom = 32.dp))

        // --- Campos de Texto ---
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
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
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); realizarLogin() }),
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
        BotonPrincipal(texto = "Iniciar sesión", onClick = { realizarLogin() })
        mensajeError?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = ErrorRed)
        }
        Spacer(Modifier.height(16.dp))
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            BotonLink(texto = "¿No tienes cuenta? Regístrate aquí", onClick = irARegistro)
        }
    }
}
