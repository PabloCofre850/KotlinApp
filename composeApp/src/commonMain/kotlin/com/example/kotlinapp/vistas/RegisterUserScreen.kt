package com.example.kotlinapp.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.data.RegionesYCiudades
import com.example.kotlinapp.models.Cliente
import com.example.kotlinapp.ui.theme.*
import com.example.kotlinapp.vistas.components.BotonPrincipal
import com.example.kotlinapp.vistas.components.BotonSecundario
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserScreen(
    irALogin: () -> Unit,
    clienteRepository: ClienteRepository
) {
    var username by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    var regionSeleccionada by remember { mutableStateOf("") }
    var ciudadSeleccionada by remember { mutableStateOf("") }
    
    var regionExpanded by remember { mutableStateOf(false) }
    var ciudadExpanded by remember { mutableStateOf(false) }

    var mensaje by remember { mutableStateOf<String?>(null) }
    var registroExitoso by remember { mutableStateOf(false) } // Nuevo estado para controlar la navegación

    val focusManager = LocalFocusManager.current

    val regiones = RegionesYCiudades.regiones.keys.toList()
    val ciudades = RegionesYCiudades.regiones[regionSeleccionada] ?: emptyList()

    // Efecto para navegar automáticamente tras el éxito
    LaunchedEffect(registroExitoso) {
        if (registroExitoso) {
            delay(1500) // Espera 1.5 segundos para que el usuario lea el mensaje
            irALogin()
        }
    }

    fun esEmailValido(email: String): Boolean = email.contains("@") && email.contains(".")

    fun realizarRegistro() {
        if (username.isBlank() || nombres.isBlank() || apellidos.isBlank() || email.isBlank() || password.isBlank() || regionSeleccionada.isBlank() || ciudadSeleccionada.isBlank()) {
            mensaje = "Todos los campos son obligatorios."
            return
        }
        if (!esEmailValido(email)) {
            mensaje = "El formato del correo no es válido."
            return
        }
        val nuevoCliente = Cliente(username, nombres, apellidos, email, ciudadSeleccionada, regionSeleccionada, password)
        if (clienteRepository.registrarCliente(nuevoCliente)) {
            mensaje = "¡Registro exitoso! Redirigiendo al login..."
            registroExitoso = true // Activa la navegación automática
        } else {
            mensaje = "El username o email ya están en uso."
        }
    }

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
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(40.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Regístrate!", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold), color = RecycleGreenPrimary)
            Spacer(Modifier.width(8.dp))
            Text("♻️", fontSize = 40.sp)
        }
        Text("Únete al cambio y comienza a reciclar", style = MaterialTheme.typography.bodyLarge, color = RecycleGreenDark, modifier = Modifier.padding(bottom = 24.dp))

        val onNext = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
        val keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)

        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, singleLine = true, keyboardOptions = keyboardOptions, keyboardActions = onNext)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = nombres, onValueChange = { nombres = it }, label = { Text("Nombres") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, singleLine = true, keyboardOptions = keyboardOptions, keyboardActions = onNext)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = apellidos, onValueChange = { apellidos = it }, label = { Text("Apellidos") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, singleLine = true, keyboardOptions = keyboardOptions, keyboardActions = onNext)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, singleLine = true, keyboardOptions = keyboardOptions, keyboardActions = onNext, isError = email.isNotBlank() && !esEmailValido(email))
        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded = regionExpanded, onExpandedChange = { regionExpanded = !regionExpanded }) {
            OutlinedTextField(value = regionSeleccionada, onValueChange = {}, readOnly = true, label = { Text("Región") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth(), colors = textFieldColors)
            ExposedDropdownMenu(expanded = regionExpanded, onDismissRequest = { regionExpanded = false }) {
                regiones.forEach { region -> DropdownMenuItem(text = { Text(region) }, onClick = { regionSeleccionada = region; ciudadSeleccionada = ""; regionExpanded = false }) }
            }
        }
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(expanded = ciudadExpanded, onExpandedChange = { ciudadExpanded = !ciudadExpanded }) {
            OutlinedTextField(value = ciudadSeleccionada, onValueChange = {}, readOnly = true, label = { Text("Ciudad") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ciudadExpanded) }, modifier = Modifier.menuAnchor().fillMaxWidth(), enabled = regionSeleccionada.isNotEmpty(), colors = textFieldColors)
            ExposedDropdownMenu(expanded = ciudadExpanded, onDismissRequest = { ciudadExpanded = false }) {
                ciudades.forEach { ciudad -> DropdownMenuItem(text = { Text(ciudad) }, onClick = { ciudadSeleccionada = ciudad; ciudadExpanded = false }) }
            }
        }
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth(), colors = textFieldColors, singleLine = true, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); realizarRegistro() }))

        Spacer(Modifier.height(24.dp))
        BotonPrincipal(texto = "Registrar", onClick = { realizarRegistro() })
        Spacer(Modifier.height(8.dp))
        BotonSecundario(texto = "Volver a Login", onClick = irALogin)

        mensaje?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = if (it.contains("exitoso")) RecycleGreenPrimary else ErrorRed)
        }
        
        Spacer(Modifier.height(40.dp))
    }
}
