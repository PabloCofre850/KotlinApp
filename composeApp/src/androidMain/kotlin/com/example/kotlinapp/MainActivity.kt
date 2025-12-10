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
import com.example.kotlinapp.models.GeminiImageClient
import com.example.kotlinapp.models.ItemReciclaje
import com.example.kotlinapp.models.ResultadoGemini
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {

    // --- EXTRAE UN JSON VÁLIDO DESDE UNA RESPUESTA QUE TIENE TEXTO EXTRA ---
    private fun extractJson(raw: String): String? {
        val start = raw.indexOf('{')
        val end = raw.lastIndexOf('}')
        return if (start != -1 && end != -1 && end > start) {
            raw.substring(start, end + 1)
        } else null
    }

    // --- DETECTA SI LA IMAGEN ES DEMASIADO OSCURA (PROBLEMA TÍPICO EN EMULADOR) ---
    private fun isImageTooDark(image: ImageBitmap): Boolean {
        val bmp = image.asAndroidBitmap()
        var total = 0L
        var count = 0

        for (x in 0 until bmp.width step 20) {
            for (y in 0 until bmp.height step 20) {
                val pixel = bmp.getPixel(x, y)
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF
                val brightness = (r + g + b) / 3
                total += brightness
                count++
            }
        }

        val avg = total / count
        return avg < 40
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            // Estados principales
            var photo by remember { mutableStateOf<ImageBitmap?>(null) }
            var geminiText by remember { mutableStateOf("") }
            var listaReciclaje by remember { mutableStateOf<List<ItemReciclaje>>(emptyList()) }
            var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }

            // Manejo de errores
            var errorTitle by remember { mutableStateOf<String?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }

            val scope = rememberCoroutineScope()
            val geminiClient = remember { GeminiImageClient() }

            fun showError(title: String, message: String) {
                errorTitle = title
                errorMessage = message
            }

            // --- FLUJO AUTOMÁTICO DE ENVÍO A GEMINI ---
            val sendToGemini: (ImageBitmap) -> Unit = { image ->
                scope.launch {

                    // 1) Verificar imagen oscura en emuladores
                    if (isImageTooDark(image)) {
                        showError(
                            "Imagen no válida",
                            "La cámara del emulador produce imágenes muy oscuras. Prueba en un dispositivo real."
                        )
                        return@launch
                    }

                    // 2) Llamar a Gemini
                    val raw = try {
                        geminiClient.generateFromImage(
                            image.asAndroidBitmap(),
                            """
                            Devuelve SOLO este JSON EXACTO:
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
                        showError(
                            "Error de Red",
                            "Gemini no respondió. Intenta nuevamente."
                        )
                        return@launch
                    }

                    geminiText = raw

                    // 3) Extraer JSON limpio
                    val cleanJson = extractJson(raw)
                    if (cleanJson == null) {
                        showError(
                            "Respuesta inválida",
                            "No se pudo extraer JSON válido desde la respuesta."
                        )
                        return@launch
                    }

                    // 4) Parsear JSON limpio
                    try {
                        val parsed = Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                        }.decodeFromString(ResultadoGemini.serializer(), cleanJson)

                        listaReciclaje = parsed.items
                        pantallaActual = Pantalla.RESULTADOS

                    } catch (e: Exception) {
                        showError(
                            "Error al procesar",
                            "La estructura JSON no coincide. Intenta con otra foto."
                        )
                    }
                }
            }

            // --- MANEJO DE PERMISOS ---
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (!granted) {
                    showError("Permiso denegado", "Se requiere acceso a la cámara.")
                }
            }

            // --- CÁMARA (FLUJO AUTOMÁTICO) ---
            val cameraLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicturePreview()
            ) { bitmap ->
                if (bitmap != null) {
                    val img = bitmap.asImageBitmap()
                    photo = img
                    sendToGemini(img) // Auto-procesamiento
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
                        errorTitle = errorTitle,
                        errorMessage = errorMessage,
                        onDismissError = {
                            errorTitle = null
                            errorMessage = null
                        }
                    )
                }
            }
        }
    }
}
