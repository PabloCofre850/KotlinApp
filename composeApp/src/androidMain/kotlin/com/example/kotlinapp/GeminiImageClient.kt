package com.example.kotlinapp.models

import android.graphics.Bitmap
import com.example.kotlinapp.GeminiConfig
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiImageClient {

    private val model = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = GeminiConfig.GEMINI_API_KEY
    )

    suspend fun generateFromImage(
        bitmap: Bitmap,
        userPrompt: String
    ): String = withContext(Dispatchers.IO) {

        val input = com.google.ai.client.generativeai.type.content {
            image(bitmap)
            text(userPrompt)
        }

        val response = model.generateContent(input)
        response.text ?: "No se recibió respuesta desde Gemini"
    }
}
