package com.example.kotlinapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.models.GeminiImageClient
import com.example.kotlinapp.models.ItemReciclaje
import com.example.kotlinapp.models.ResultadoGemini
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            var photo by remember { mutableStateOf<ImageBitmap?>(null) }
            var geminiText by remember { mutableStateOf("") }
            var listaReciclaje by remember { mutableStateOf<List<ItemReciclaje>>(emptyList()) } // Corregido: mutableStateOf
            var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }

            var errorTitle by remember { mutableStateOf<String?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            val scope = rememberCoroutineScope()
            val geminiClient = remember { GeminiImageClient() }
            val clienteRepository = remember { ClienteRepository() }

            fun showError(title: String, message: String) {
                errorTitle = title
                errorMessage = message
            }

            val sendToGemini: (ImageBitmap) -> Unit = { image ->
                scope.launch {
                    val result = try {
                        geminiClient.generateFromImage(
                            image.asAndroidBitmap(),
                            """
                            Devuelve SOLO este JSON:
                            {
                              "items":[
                                {"nombre":"...","material":"...","colorBasurero":"..."}
                              ]
                            }
                            Si nada es reciclable:
                            {"items":[]}
                            """.trimIndent()
                        )
                    } catch (e: Exception) {
                        showError("Error de Red", "Gemini no respondió. Revisa tu conexión e intenta de nuevo.")
                        return@launch
                    }

                    geminiText = result

                    try {
                        val cleanJson = result.substringAfter("{").substringBeforeLast("}").let { "{${it}}" }
                        val parsed = Json.decodeFromString(ResultadoGemini.serializer(), cleanJson)

                        if (parsed.items.isEmpty()) {
                            showError("Sin Resultados", "No pudimos identificar ningún objeto reciclable en la imagen.")
                        } else {
                            listaReciclaje = parsed.items
                            pantallaActual = Pantalla.RESULTADOS
                        }
                    } catch (e: Exception) {
                        showError("Error de Procesamiento", "No se pudo entender la respuesta del servidor. Intenta con otra imagen.")
                    }
                }
            }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (!granted) {
                    showError("Permiso Requerido", "El permiso de la cámara es necesario para escanear objetos.")
                }
            }

            val cameraLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicturePreview()
            ) { bitmap ->
                if (bitmap != null) {
                    val imageBitmap = bitmap.asImageBitmap()
                    photo = imageBitmap
                    sendToGemini(imageBitmap)
                }
            }

            MaterialTheme {
                Scaffold { padding ->
                    App(
                        modifierPadding = padding,
                        photo = photo,
                        geminiText = geminiText,
                        listaReciclaje = listaReciclaje,
                        pantallaActual = pantallaActual,
                        onChangePantalla = { pantallaActual = it },
                        onOpenCamera = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                            cameraLauncher.launch(null)
                        },
                        onSendToGemini = {},
                        errorTitle = errorTitle,
                        errorMessage = errorMessage,
                        onDismissError = {
                            errorTitle = null
                            errorMessage = null
                        },
                        clienteRepository = clienteRepository
                    )
                }
            }
        }
    }
}
