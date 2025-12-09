package com.example.kotlinapp.vistas

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.vistas.components.BotonSecundario
import com.example.kotlinapp.models.Cliente
import com.example.kotlinapp.vistas.components.BotonPrincipal

@Composable
fun RegisterUserScreen(
    repo: ClienteRepository,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registro de Cliente",
            style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = rut, onValueChange = { rut = it }, label = { Text("RUT") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = apellidoP, onValueChange = { apellidoP = it }, label = { Text("Apellido Paterno") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = apellidoM, onValueChange = { apellidoM = it }, label = { Text("Apellido Materno") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        BotonPrincipal(
            texto = "Registrar",
            onClick = {
                val cliente = Cliente(
                    idCliente = 0,
                    ciudad = ciudad,
                    tieneDeuda = false,
                    password = password,
                    rut = rut,
                    nombre = nombre,
                    apellidoPaterno = apellidoP,
                    apellidoMaterno = apellidoM,
                    email = email
                )

                val ok = repo.registrarCliente(cliente)

                mensaje = if (ok) {
                    "Cliente registrado correctamente."
                } else {
                    "Ya existe un cliente con ese RUT o correo."
                }

                if (ok) {
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
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
