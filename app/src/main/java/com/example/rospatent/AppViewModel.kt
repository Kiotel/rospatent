package com.example.rospatent

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rospatent.classes.Patent
import com.example.rospatent.classes.RosPatentResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.InternalAPI
import kotlinx.serialization.json.Json

const val token = BuildConfig.api_key

class AppViewModel : ViewModel() {

    private var _patents: List<Patent> = emptyList()

    private lateinit var _lastQuery: String
    private lateinit var _lastLang: String
    private lateinit var _lastSortOption: String
    private var _total = 0
    private var _available = 0

    private var _offset = 0

    val isSearched = MutableLiveData<Boolean>(false)

    val isLoading = MutableLiveData<Boolean>(false)
//  val isLoading: LiveData<Boolean>
//    get() = _isLoading

    private val _isEnd = MutableLiveData<Boolean>(false)
    val isEnd: LiveData<Boolean>
        get() = _isEnd


    val patents: List<Patent>
        get() {
            return _patents
        }

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    private val url = "https://searchplatform.rospatent.gov.ru/patsearch/v0.2/"

    fun clearPatents() {
        _patents = emptyList()
        _offset = 0
        isSearched.value = false
    }

    suspend fun loadMore() {
        if (_available - _offset >= 100) {
            _offset += 50
        } else {
            _isEnd.value = true
            return
        }
        _patents += makeRequest(
            q = _lastQuery,
            lang = _lastLang,
            sort = _lastSortOption,
            offset = _offset
        )
    }


    suspend fun searchPatents(
        q: String = "",
        lang: String = "ru",
        sort: String = "",
        limit: Int = 50,
    ) {
        _patents = makeRequest(q, lang, sort, limit, 0)
    }

    @OptIn(InternalAPI::class)
    suspend fun makeRequest(
        q: String = "",
        lang: String = "ru",
        sort: String = "",
        limit: Int = 50,
        offset: Int = 0,
    ): List<Patent> {
        try {
            isLoading.value = true

            _lastQuery = q
            _lastLang = lang
            _lastSortOption = sort

            val json = """
    {
    "qn": "$q"
    ${
                if (lang != "") {
                    ",\"lang\": \"$lang\""
                } else {
                    ""
                }
            }
    ${
                if (sort != "") {
                    ",\"sort\": \"$sort\""
                } else {
                    ""
                }
            }
    ${
                ",\"offset\": $offset"
            }
    ,"limit": $limit
    }
    """.trimIndent()

            Log.d("--------------->", json)


            val response = client.post("${url}search") {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                setBody(json)
            }

            // Log.d("AppViewModel body", response.body())

            val responseJson: RosPatentResponse = response.body()
            val patents = responseJson.patents

            _total = responseJson.total

            _available = responseJson.available

            if (_available > 50) {
                Log.d("->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", "Available")
                _isEnd.value = false
            } else {
                Log.d("->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", "Not Available")
                _isEnd.value = true
            }

            Log.d("AppViewModel header", response.headers.toString())
            Log.d("AppViewModel request", response.request.toString())
            Log.d("AppViewModel content", response.content.toString())
            Log.d("AppViewModel body", json)
            Log.d("AppViewModel status", response.status.toString())
            Log.d("Total:", responseJson.total.toString())
            Log.d("Available:", responseJson.available.toString())
            Log.d("JSON", responseJson.toString())


            patents.forEach { patent ->
                Log.d("PATENT", patent.snippet.title)
                patent.snippet.title = patent.snippet.title.replace("<em>", "")
                patent.snippet.title = patent.snippet.title.replace("</em>", "")

                patent.snippet.description =
                    patent.snippet.description.replace("<em>", "")
                patent.snippet.description =
                    patent.snippet.description.replace("</em>", "")
            }
            // Log.d("PATENTS", patents.toString())
        } catch (e: Exception) {
            Log.e("Error in server response", e.toString())
        }

        isLoading.value = false
        isSearched.value = true

        return patents
    }
}
