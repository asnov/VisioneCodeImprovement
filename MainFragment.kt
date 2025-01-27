package com.example.testfragments.provided

import com.example.testfragments.ui.main.Constant
import com.example.testfragments.ui.main.MainFragment
import java.io.File

/**********************************************/

val fields = listOf(
    "Company",
    "Website",
    "Name",
    "Title",
    "Email",
    "Phone",
    "Mobile",
    "Address",
    "City",
    "State",
    "PostalCode",
    "Country"
)

/**********************************************/

fun MainFragment.callGemini(onSuccess: () -> Unit, onError: (err: String) -> Unit) {
    var query = OcrUtils.generateStructuredDataStringGemini(fields)
    OcrUtils.fetchOcrDataUsingGemini(
        requireContext(),
        File(
            requireContext().cacheDir,
            Constant.tempImagename
        ),
        values,
        fields,
        query,
        onSuccess = { map ->
            processCustomFields(map)
            onSuccess()
        },
        onError = { error ->
            onError(error)
        })
}

/**********************************************/
