package com.buddyai.app

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object GeminiClient {

    fun askBuddy(question: String): String {
        var connection: HttpURLConnection? = null

        try {
            val url = URL(
                "https://buddy-ai-backend-vzkw.onrender.com/ask"
            )

            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.connectTimeout = 20000
            connection.readTimeout = 60000
            connection.doOutput = true

            connection.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8"
            )

            val body = JSONObject().put("question", question)

            connection.outputStream.use { output ->
                output.write(body.toString().toByteArray(Charsets.UTF_8))
            }

            val status = connection.responseCode

            val stream = if (status in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            }

            val response = stream?.bufferedReader()?.use {
                it.readText()
            } ?: ""

            val json = JSONObject(response)

            if (status !in 200..299) {
                return json.optString(
                    "error",
                    "Buddy se connect nahi ho paya. Dobara try karo."
                )
            }

            return json.optString(
                "answer",
                "Buddy ko jawab nahi mila."
            )

        } catch (e: Exception) {
            return "Connection problem: Internet check karo aur dobara try karo."
        } finally {
            connection?.disconnect()
        }
    }
}
