package com.tx.edusphere.data.remote

import com.tx.edusphere.BuildConfig
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiService @Inject constructor(
    private val httpClient: HttpClient
) {
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    companion object {
        private val MODELS = listOf(
            "gemini-2.5-flash",
            "gemini-2.0-flash",
            "gemini-1.5-flash-latest",
            "gemini-2.0-flash-lite",
            "gemini-1.5-pro"
        )
    }

    suspend fun generateContent(prompt: String, systemInstruction: String? = null): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        var lastErrorMessage = ""

        for (model in MODELS) {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"

            val requestBody = buildJsonObject {
                putJsonArray("contents") {
                    addJsonObject {
                        putJsonArray("parts") {
                            addJsonObject {
                                put("text", prompt)
                            }
                        }
                    }
                }
                if (!systemInstruction.isNullOrBlank()) {
                    putJsonObject("systemInstruction") {
                        putJsonArray("parts") {
                            addJsonObject {
                                put("text", systemInstruction)
                            }
                        }
                    }
                }
            }

            try {
                val response: HttpResponse = httpClient.post(url) {
                    contentType(ContentType.Application.Json)
                    setBody(requestBody.toString())
                }

                if (response.status.isSuccess()) {
                    val responseText = response.bodyAsText()
                    val parsed = json.parseToJsonElement(responseText).jsonObject
                    val candidates = parsed["candidates"]?.jsonArray
                    val firstCandidate = candidates?.firstOrNull()?.jsonObject
                    val content = firstCandidate?.get("content")?.jsonObject
                    val parts = content?.get("parts")?.jsonArray
                    val firstPart = parts?.firstOrNull()?.jsonObject
                    val generatedText = firstPart?.get("text")?.jsonPrimitive?.content
                    if (!generatedText.isNullOrBlank()) {
                        return generatedText
                    }
                } else {
                    lastErrorMessage = response.bodyAsText()
                }
            } catch (e: Exception) {
                lastErrorMessage = e.message ?: "Connection error"
            }
        }

        return "AI Assistant Error: Could not reach Gemini model endpoint. Details: $lastErrorMessage"
    }
}
