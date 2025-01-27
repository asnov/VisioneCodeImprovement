package com.example.testfragments.provided

import android.content.Context
import com.example.testfragments.ui.main.gemini.GeminiRequest
import com.example.testfragments.ui.main.gemini.GeminiResponse


// contract for data operations related to Gemini results
interface DataRepository {

    /**********************************************/

    fun getGeminiResult(
        context: Context,
        geminiRequest: GeminiRequest,
        apiKey: String,
        onSuccess: (response: GeminiResponse) -> Unit,
        onError: (t: Throwable) -> Unit
    )

    /**********************************************/

}
