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
            var photo by remember { mutableStateOf<ImageBitmap?>(null) }
            var geminiText by remember { mutableStateOf("") }
            var listaReciclaje by remember { mutableStateOf<List<ItemReciclaje>>(emptyList()) }
            var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }

            var errorTitle by remember { mutableStateOf<String?>(null) }
            var errorMessage by remember { mutableStateOf<String?>(null) }
            
            var isLoading by remember { mutableStateOf(false) }

            val scope = rememberCoroutineScope()
            val context = LocalContext.current

            val clienteRepository = remember {
                val fileStorage = FileStorage(context)
                val persistencia = PersistenciaLocal(fileStorage)
                ClienteRepository(persistencia)
            }

            val geminiClient = remember { GeminiImageClient() }

            fun showError(title: String, message: String) {
                errorTitle = title
                errorMessage = message
            }

            val sendToGemini: (ImageBitmap) -> Unit = { image ->
                scope.launch {
                    isLoading = true
                    try {
                        val result = geminiClient.generateFromImage(
                            image.asAndroidBitmap(),
                            """
                            Analiza la imagen y detecta objetos reciclables en el contexto de Chile.
                            Devuelve SOLO un JSON con esta estructura:
                            {
                              "items": [
                                {
                                  "nombre": "Nombre del objeto",
                                  "material": "Material principal",
                                  "colorBasurero": "Color",
                                  "instrucciones": "Texto con los pasos."
                                }
                              ]
                            }

                            Reglas para 'colorBasurero' (Norma Chilena):
                            - "Azul" para Papel y Cartón.
                            - "Amarillo" para Plásticos y PET.
                            - "Verde" para Vidrio.
                            - "Marrón" para Orgánicos.
                            - "Gris" para Restos/No reciclable.
                            - "Rojo" para Peligrosos.

                            Reglas para 'instrucciones':
                            - Usa un tono FORMAL y claro.
                            - Usa terminología de Chile (ej: "Punto Limpio", "Municipalidad"). EVITA términos como "Ayuntamiento" o "Fregadero".
                            - Sé BREVE. Máximo 3 líneas.
                            - Usa '\n' para separar los pasos.
                            - Ejemplo: "1. Lave y seque el envase.\n2. Aplaste para reducir volumen.\n3. Deposite en el contenedor [Color]."

                            Si no hay objetos reciclables, devuelve {"items": []}.
                            """.trimIndent()
                        )

                        geminiText = result
                        val cleanJson = result.substringAfter("{").substringBeforeLast("}").let { "{${it}}" }
                        val parsed = Json.decodeFromString(ResultadoGemini.serializer(), cleanJson)

                        if (parsed.items.isEmpty()) {
                            showError("Sin Resultados", "No pudimos identificar ningún objeto reciclable en la imagen.")
                        } else {
                            listaReciclaje = parsed.items
                            pantallaActual = Pantalla.RESULTADOS
                        }

                    } catch (e: Exception) {
                        // Captura errores de red o de parseo
                        showError("Error", "No se pudo procesar la imagen. Revisa tu conexión o intenta con otra foto.")
                    } finally {
                        isLoading = false // Se ejecuta siempre
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
                        clienteRepository = clienteRepository,
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}
