package com.example.testfragments.provided

import android.content.Context
import android.util.Log
import com.example.testfragments.ui.main.gemini.Content
import com.example.testfragments.ui.main.gemini.GeminiRequest
import com.example.testfragments.ui.main.gemini.InlineData
import com.example.testfragments.ui.main.gemini.Part
import com.example.testfragments.ui.main.utils.CommonUtils
import com.example.testfragments.ui.main.utils.DialogUtils
import com.example.testfragments.ui.main.utils.fileToBase64String
import com.example.testfragments.ui.main.utils.parseAndMapJsonData
import org.json.JSONException
import org.json.JSONObject
import java.io.File

private const val TAG = ">> OcrUtils"


object OcrUtils {

    /**********************************************/

    fun generateStructuredDataStringGemini(keys: List<String>): String {
        val extractedKeys = keys.joinToString(", ")
        // AI instructions as provided
        val aiInstructions = """
Extract text from the provided business card image and generate only a JSON string
with the following properties:
$extractedKeys
Ensure the values are directly derived or inferred from the text where possible do not
translate to english. Do not translate name to english.
If a property cannot be identified, do not provide results.
Use proper formatting for email addresses, phone numbers, and postal codes. When
providing proper format do not comment.
""".trimIndent()
        return aiInstructions
    }

    /**********************************************/

    fun fetchOcrDataUsingGemini(
        context: Context,
        file: File,
        values: MutableList<String>,
        fields: List<String>,
        query: String,
        onSuccess: (Map<String,String>) -> Unit,
        onError: (errorMsg: String) -> Unit
    ) {
        val base64Image = fileToBase64String(file)
        val request = GeminiRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = query),
                        Part(
                            inlineData = InlineData(
                                mimeType = "image/jpeg",
                                data = base64Image
                            )
                        )
                    )
                )
            )
        )
        val dialog = DialogUtils.showLoading(context, "Loading...")
        dialog.show()
        val dataRepository = DataRepositoryImpl()
        dataRepository.getGeminiResult(context, request, CommonUtils.geminiToken,
            onSuccess = { response ->
                try {
                    // Extract JSON string from the response
                    val jsonString = response.candidates
                        ?.firstOrNull()
                        ?.content
                        ?.parts
                        ?.firstOrNull { it.text != null }
                        ?.text
                        ?.substringAfter("```json")
                        ?.substringBefore("```")
                        ?.trim()
                    if (!jsonString.isNullOrEmpty()) {
                        val jsonObject = JSONObject(jsonString)
                        parseAndMapJsonData(jsonObject, fields, values)
                        onSuccess(CommonUtils.jsonObjectToMap(jsonObject))
                    } else {
                        println("No valid JSON found in the response.")
                        onError("Failed to parse response data.")
                    }
                } catch (e: JSONException) {
                    println("Error parsing JSON: ${e.message}")
                    onError("Failed to parse response data.")
                } finally {
                    dialog.dismiss()
                }
            },
            onError = { errorMsg ->
                dialog.dismiss()
                onError(errorMsg.message.orEmpty())
            }
        )
    }

    /**********************************************/

}
