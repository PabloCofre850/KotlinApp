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
import androidx.compose.ui.platform.LocalContext
import com.example.kotlinapp.data.ClienteRepository
import com.example.kotlinapp.data.FileStorage
import com.example.kotlinapp.data.PersistenciaLocal
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

            // ---------- ESTADOS ----------
            var photo by remember { mutableStateOf<ImageBitmap?>(null) }
            var geminiText by remember { mutableStateOf("") }
            var listaReciclaje by remember { mutableStateOf<List<ItemReciclaje>>(emptyList()) }
            var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }

            var errorTitle by remember { mutableStateOf<String?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }
            var isLoading by remember { mutableStateOf(false) }

            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            // ---------- REPOSITORIO ----------
            val clienteRepository = remember {
                val fileStorage = FileStorage(context)
                val persistencia = PersistenciaLocal(fileStorage)
                ClienteRepository(persistencia)
            }

            // ---------- CLIENTE GEMINI ----------
            val geminiClient = remember { GeminiImageClient() }

            fun showError(title: String, message: String) {
                errorTitle = title
                errorMessage = message
            }

            // ---------- FUNCIÓN PARA ENVIAR FOTO A GEMINI ----------
            val sendToGemini: (ImageBitmap) -> Unit = { imageBitmap ->

                scope.launch {
                    isLoading = true

                    try {
                        val result = geminiClient.generateFromImage(
                            bitmap = imageBitmap.asAndroidBitmap(),
                            userPrompt = """
                                Analiza la imagen y detecta objetos reciclables presentes.

                                Devuelve EXCLUSIVAMENTE un JSON con esta estructura exacta:

                                {
                                  "items": [
                                    {
                                      "nombre": "Texto",
                                      "material": "Plástico|Papel|Cartón|Vidrio|Metal|Orgánico|NoReciclable",
                                      "colorBasurero": "Amarillo|Azul|Verde|Marrón|Gris|Rojo",
                                      "instrucciones": "Texto breve con pasos"
                                    }
                                  ]
                                }

                                Reglas:
                                - NO devuelvas "Desconocido". Si no estás seguro, elige el material más probable.
                                - No uses ```json ni explicaciones fuera del JSON.
                                - Si no hay objetos reciclables, devuelve {"items": []}.
                            """.trimIndent()
                        )

                        geminiText = result

                        // ---------------- LIMPIADOR DE JSON ----------------
                        val cleanJson = result
                            .trim()
                            .removePrefix("```json")
                            .removePrefix("```")
                            .removeSuffix("```")
                            .trim()

                        val parsed = try {
                            Json.decodeFromString(ResultadoGemini.serializer(), cleanJson)
                        } catch (e: Exception) {
                            showError("Error en IA", "La respuesta no es válida. Intenta otra foto.")
                            ResultadoGemini(emptyList())
                        }

                        if (parsed.items.isEmpty()) {
                            showError("Sin resultados", "No se detectaron objetos reciclables.")
                        } else {
                            listaReciclaje = parsed.items
                            pantallaActual = Pantalla.RESULTADOS
                        }

                    } catch (e: Exception) {
                        showError("Error", "No se pudo procesar la imagen. Intenta de nuevo.")
                    } finally {
                        isLoading = false
                    }
                }
            }

            // ---------- LAUNCHER DE CÁMARA ----------
            val cameraLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicturePreview()
            ) { bitmap ->
                if (bitmap != null) {
                    val imageBitmap = bitmap.asImageBitmap()
                    photo = imageBitmap
                    sendToGemini(imageBitmap)
                }
            }

            // ---------- LAUNCHER DE PERMISO ----------
            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (granted) {
                    cameraLauncher.launch(null)   // SOLO AQUÍ SE ABRE LA CÁMARA
                } else {
                    showError("Permiso requerido", "La cámara es necesaria para continuar.")
                }
            }

            // ---------- INTERFAZ ----------

            MaterialTheme {
                Scaffold { padding ->
                    App(
                        modifierPadding = padding,
                        photo = photo,
                        geminiText = geminiText,
                        listaReciclaje = listaReciclaje,
                        pantallaActual = pantallaActual,
                        onChangePantalla = { pantallaActual = it },

                        // Botón de HomeScreen
                        onOpenCamera = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },

                        onSendToGemini = {},

                        errorTitle = errorTitle,
                        errorMessage = errorMessage,
                        onDismissError = {
                            errorTitle = null
                            errorMessage = null
                        },

                        clienteRepository = clienteRepository,
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}
