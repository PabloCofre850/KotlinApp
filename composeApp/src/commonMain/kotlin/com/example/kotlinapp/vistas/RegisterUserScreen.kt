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
import com.example.kotlinapp.vistas.components.BotonPrincipal
import com.example.kotlinapp.vistas.components.BotonSecundario

@Composable
fun RegisterUserScreen(
    irALogin: () -> Unit
) {
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apellidoP by remember { mutableStateOf("") }
    var apellidoM by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf<String?>(null) }

    // Configuración de colores
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = RecycleGreenPrimary,
        unfocusedBorderColor = RecycleGreenMedium,
        cursorColor = RecycleGreenPrimary,
        focusedLabelColor = RecycleGreenPrimary,
        unfocusedLabelColor = RecycleGreenMedium,
        focusedContainerColor = RecycleWhite,
        unfocusedContainerColor = RecycleWhite
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(RecycleWhite)
            .padding(24.dp),
        horizontalAlignment = Alignment.Start, // Alineación a la izquierda para el título
        verticalArrangement = Arrangement.Center // Centrado verticalmente
    ) {
        // Título con Emoji
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text(
                text = "Regístrate!",
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = RecycleGreenPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "♻️", // Emoji de reciclaje
                fontSize = 40.sp
            )
        }

        Text(
            text = "Únete al cambio y comienza a reciclar",
            style = MaterialTheme.typography.bodyLarge,
            color = RecycleGreenDark,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Campos de texto
        OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = apellidoP, onValueChange = { apellidoP = it }, label = { Text("Apellido Paterno") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = apellidoM, onValueChange = { apellidoM = it }, label = { Text("Apellido Materno") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors)

        Spacer(Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Registrar",
            onClick = {
                if (rut.isBlank() || nombre.isBlank() || password.isBlank()) {
                    mensaje = "Debes completar todos los campos"
                } else {
                    mensaje = "Cliente registrado correctamente."
                    irALogin()
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        BotonSecundario(
            texto = "Volver a Login",
            onClick = irALogin
        )

        mensaje?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = if (it.contains("correctamente")) RecycleGreenPrimary else ErrorRed)
        }
    }
}
