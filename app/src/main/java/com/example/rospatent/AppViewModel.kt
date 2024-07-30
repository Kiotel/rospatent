package com.example.rospatent

import android.net.http.HttpResponseCache.install
import android.util.Log
import androidx.lifecycle.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.util.InternalAPI

val token = "26a213594e7f4f6e8cd89064d885ea93"

class AppViewModel : ViewModel() {
  private val client = HttpClient()
  private val url = "https://searchplatform.rospatent.gov.ru/patsearch/v0.2/"

  @OptIn(InternalAPI::class)
  suspend fun searchPatents(q: String) {

    val json = """
      {
      	"q": "$q",
        "lang": "ru"
      }
    """.trimIndent()

    val response = client.post("${url}search") {
      contentType(ContentType.Application.Json)
      bearerAuth(token)
      setBody(json)
    }


    Log.d("AppViewModel header", response.headers.toString())
    Log.d("AppViewModel request", response.request.toString())
    Log.d("AppViewModel content", response.content.toString())
    Log.d("AppViewModel body", json)
    Log.d("AppViewModel body", response.body())
    Log.d("AppViewModel status", response.status.toString())
  }
}