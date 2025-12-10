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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
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

            val snackbarHostState = remember { SnackbarHostState() }

            var photo by remember { mutableStateOf<ImageBitmap?>(null) }
            var geminiResponse by remember { mutableStateOf("") }
            var listaReciclaje by remember { mutableStateOf<List<ItemReciclaje>>(emptyList()) }
            var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }

            val scope = rememberCoroutineScope()
            val geminiClient = remember { GeminiImageClient() }

            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (!granted) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Permiso de cámara denegado")
                    }
                }
            }

            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicturePreview()
            ) { bitmap ->
                if (bitmap != null) {
                    photo = bitmap.asImageBitmap()
                }
            }

            MaterialTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { padding ->

                    App(
                        modifierPadding = padding,
                        photo = photo,
                        geminiText = geminiResponse,
                        listaReciclaje = listaReciclaje,
                        pantallaActual = pantallaActual,
                        onChangePantalla = { nueva ->
                            pantallaActual = nueva
                        },
                        onOpenCamera = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                            cameraLauncher.launch(null)
                        },
                        onSendToGemini = {
                            val img = photo
                            if (img == null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Primero toma una foto")
                                }
                                return@App
                            }

                            val bitmap = img.asAndroidBitmap()

                            scope.launch {
                                val result = geminiClient.generateFromImage(
                                    bitmap,
                                    """
                                    Analiza los objetos reciclables de la imagen.
                                    Devuélveme SOLO el siguiente JSON válido:
                                    
                                    {
                                        "items": [
                                            {
                                                "nombre": "...",
                                                "material": "...",
                                                "colorBasurero": "..."
                                            }
                                        ]
                                    }
                                    
                                    Solo JSON, sin texto adicional.
                                    """.trimIndent()
                                )

                                geminiResponse = result

                                try {
                                    val parsed = Json.decodeFromString(ResultadoGemini.serializer(), result)
                                    listaReciclaje = parsed.items
                                    pantallaActual = Pantalla.RESULTADOS
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar("Error al interpretar JSON de Gemini")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
