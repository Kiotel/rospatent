package com.example.rospatent

import android.util.Log
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.InternalAPI
import io.ktor.client.*
import kotlinx.serialization.json.Json

const val token = "26a213594e7f4f6e8cd89064d885ea93"

class AppViewModel : ViewModel() {
  private val client = HttpClient() {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
      })
    }
  }
  private val url = "https://searchplatform.rospatent.gov.ru/patsearch/v0.2/"


  @OptIn(InternalAPI::class)
  suspend fun searchPatents(q: String = "", lang: String = ""): List<Patent> {


    val json = """
    {
    "q": "$q"
    ${
      if (lang != "") {
        ",lang: \"$lang\""
      } else {
        ""
      }
    }
    }
    """.trimIndent()

    val response = client.post("${url}search") {
      contentType(ContentType.Application.Json)
      bearerAuth(token)
      setBody(json)
    }

    // Log.d("AppViewModel body", response.body())

    val responseJson: RosPatentResponse = response.body()
    val patents = responseJson.patents

    Log.d("AppViewModel header", response.headers.toString())
    Log.d("AppViewModel request", response.request.toString())
    Log.d("AppViewModel content", response.content.toString())
    Log.d("AppViewModel body", json)
    Log.d("AppViewModel status", response.status.toString())

    Log.d("JSON", responseJson.toString())
    patents.forEach { patent ->
      Log.d("PATENT", patent.snippet.title)
      patent.snippet.title = patent.snippet.title.replace("<em>", "")
      patent.snippet.title = patent.snippet.title.replace("</em>", "")

      patent.snippet.description = patent.snippet.description.replace("<em>", "")
      patent.snippet.description = patent.snippet.description.replace("</em>", "")
    }
    // Log.d("PATENTS", patents.toString())

    return patents
  }
}
