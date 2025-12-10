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

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {

            val snackbarHostState = remember { SnackbarHostState() }

            var photo by remember { mutableStateOf<ImageBitmap?>(null) }
            var geminiText by remember { mutableStateOf("") }
            var listaReciclaje by remember { mutableStateOf<List<ItemReciclaje>>(emptyList()) }
            var pantallaActual by remember { mutableStateOf(Pantalla.LOGIN) }

            val scope = rememberCoroutineScope()
            val geminiClient = remember { GeminiImageClient() }

            // Permiso cámara
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (!granted) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Permiso de cámara denegado")
                    }
                }
            }

            // Abrir cámara
            val cameraLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicturePreview()
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
                        geminiText = geminiText,
                        listaReciclaje = listaReciclaje,
                        pantallaActual = pantallaActual,
                        onChangePantalla = { pantallaActual = it },

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

                            scope.launch {

                                // --------------------------
                                // 1. LLAMADA A GEMINI PROTEGIDA
                                // --------------------------
                                val result = try {
                                    geminiClient.generateFromImage(
                                        img.asAndroidBitmap(),
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
                                    snackbarHostState.showSnackbar("Gemini no respondió. Intenta nuevamente.")
                                    return@launch
                                }

                                geminiText = result

                                // --------------------------
                                // 2. PARSEO JSON PROTEGIDO
                                // --------------------------
                                println("RESPUESTA GEMINI >>> $result")

                                try {
                                    // Limpia json de basura
                                    val cleanJson = result
                                        .substringAfter("{")
                                        .substringBeforeLast("}")
                                        .let { "{${it}}" }

                                    val parsed = Json.decodeFromString(
                                        ResultadoGemini.serializer(),
                                        cleanJson
                                    )

                                    listaReciclaje = parsed.items
                                    pantallaActual = Pantalla.RESULTADOS

                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar("Error al leer JSON de Gemini")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
