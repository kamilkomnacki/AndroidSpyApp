package com.komnacki.androidspyapp.api

import com.google.gson.annotations.SerializedName

class ApiResponse {
    @SerializedName("response")
    val response : String? = null

    override fun toString() : String {
        return "response: " + response
    }
}