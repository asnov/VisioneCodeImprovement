package com.example.testfragments.provided

import android.content.Context
import com.example.testfragments.ui.main.api.RetrofitClient
import com.example.testfragments.ui.main.api.RfApiService
import com.example.testfragments.ui.main.gemini.GeminiRequest
import com.example.testfragments.ui.main.gemini.GeminiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// repository pattern abstracting the source of data from other parts of the app (like the UI)
class DataRepositoryImpl : DataRepository {

    /**********************************************/

    override fun getGeminiResult(
        context: Context,
        geminiRequest: GeminiRequest, // Define this class based on the API structure
        apiKey: String,
        onSuccess: (response: GeminiResponse) -> Unit, // Define this class for the response structure
        onError: (t: Throwable) -> Unit
    ) {
        val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/"
        val apiService =
            RetrofitClient.getRetrofitInstance(BASE_URL).create(RfApiService::class.java)
        // Add the API key as a query parameter
        val call = apiService.generateContent(apiKey, geminiRequest)
        call.enqueue(object : Callback<GeminiResponse> {

            override fun onResponse(
                call: Call<GeminiResponse>, response:
                Response<GeminiResponse>
            ) {
                if (response.isSuccessful) {
                    val geminiResponse = response.body()
                    geminiResponse?.let {
                        println("Gemini Response: $it")
                        onSuccess(it)
                    }
                } else {
                    onError(Throwable("Error: ${response.code()} - ${response.message()}"))
                }
            }

            override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                onError(t)
            }
        })
    }

    /**********************************************/

}
