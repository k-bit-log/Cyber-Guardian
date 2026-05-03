package com.example.cyberguardian.data.remote

import com.example.cyberguardian.data.VirusTotalResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface VirusTotalApi {
    @GET("urls/{id}")
    suspend fun getUrlReport(
        @Path("id") urlId: String,
        @Header("x-apikey") apiKey: String
    ): Response<VirusTotalResponse>

    companion object {
        const val BASE_URL = "https://www.virustotal.com/api/v3/"
    }
}
