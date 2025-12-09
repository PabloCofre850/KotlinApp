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
import kotlinx.coroutines.launch
import org.example.kotlinapp.GeminiImageClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val snackbarHostState = remember { SnackbarHostState() }

            var photo by remember { mutableStateOf<ImageBitmap?>(null) }
            var geminiResponse by remember { mutableStateOf("") }

            val scope = rememberCoroutineScope()
            val geminiClient = remember { GeminiImageClient() }

            // launcher permiso de cámara
            val permissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (!granted) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Permiso de cámara denegado")
                    }
                }
            }

            // launcher cámara (foto previa)
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
                        onOpenCamera = {
                            // pedir permiso + abrir cámara
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                            cameraLauncher.launch(null)
                        },
                        onSendToGemini = {
                            val img = photo
                            if (img == null) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Primero toma una foto")
                                }
                            } else {
                                val bitmap = img.asAndroidBitmap()
                                scope.launch {
                                    val result = geminiClient.generateFromImage(
                                        bitmap = bitmap,
                                        userPrompt = "genera un diccionario de datos con los objetos que solamente se puedan reciclar de la imagen, separando por material organico, plastico, papel y carton para implementar en clases"
                                    )
                                    geminiResponse = result
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}