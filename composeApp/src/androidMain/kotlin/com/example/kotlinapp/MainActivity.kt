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
import com.example.kotlinapp.data.FileStorage
import com.example.kotlinapp.GeminiImageClient
import kotlinx.coroutines.launch

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

            val permissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { granted ->
                if (!granted) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Permiso de cámara denegado")
                    }
                }
            }

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
                        storage = FileStorage(this),
                        modifierPadding = padding,
                        photo = photo,
                        geminiText = geminiResponse,
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
                            } else {
                                scope.launch {
                                    val bitmap = img.asAndroidBitmap()
                                    val result = geminiClient.generateFromImage(
                                        bitmap = bitmap,
                                        userPrompt = "genera un diccionario con los objetos reciclables de la imagen"
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
