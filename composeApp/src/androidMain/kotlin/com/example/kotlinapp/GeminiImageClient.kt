package org.example.kotlinapp

import android.graphics.Bitmap
import com.example.kotlinapp.GeminiConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GeminiImageClient {

    // Modelo de Gemini configurado con tu API Key
    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",   // 👈 modelo nuevo soportado en v1beta
        apiKey = GeminiConfig.GEMINI_API_KEY
    )

    suspend fun generateFromImage(
        bitmap: Bitmap,
        userPrompt: String
    ): String = withContext(Dispatchers.IO) {

        val input = content {
            image(bitmap)
            text(userPrompt)
        }

        val response = model.generateContent(input)

        response.text ?: "No se recibió respuesta desde Gemini"
    }
}