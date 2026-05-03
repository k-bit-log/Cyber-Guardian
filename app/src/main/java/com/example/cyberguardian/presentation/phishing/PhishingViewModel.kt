package com.example.cyberguardian.presentation.phishing

import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyberguardian.data.PhishingScanResult
import com.example.cyberguardian.data.remote.VirusTotalApi
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhishingViewModel : ViewModel() {

    private val apiKey = "4e9c5cc78b7f79ffa10d6f6c0de9221180c0ad624cfc27ba7b2a79542162bb93" // Placeholder

    private val api = Retrofit.Builder()
        .baseUrl(VirusTotalApi.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(VirusTotalApi::class.java)

    var uiState by mutableStateOf<PhishingUiState>(PhishingUiState.Idle)
        private set

    fun scanUrl(url: String) {
        if (url.isBlank()) return

        uiState = PhishingUiState.Loading

        viewModelScope.launch {
            try {
                // VirusTotal URL ID is Base64 of the URL without padding
                val urlId = Base64.encodeToString(url.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
                
                val response = api.getUrlReport(urlId, apiKey)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    val stats = body.data.attributes.last_analysis_stats
                    val results = body.data.attributes.last_analysis_results.values.toList()

                    uiState = PhishingUiState.Success(
                        PhishingScanResult(
                            url = url,
                            isMalicious = stats.malicious > 0 || stats.suspicious > 0,
                            maliciousCount = stats.malicious,
                            totalEngines = stats.harmless + stats.malicious + stats.suspicious + stats.undetected,
                            engineResults = results
                        )
                    )
                } else {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    uiState = PhishingUiState.Error("API Error: $errorMsg")
                }
            } catch (e: Exception) {
                uiState = PhishingUiState.Error("Failed to scan: ${e.message}")
            }
        }
    }
}

sealed class PhishingUiState {
    object Idle : PhishingUiState()
    object Loading : PhishingUiState()
    data class Success(val result: PhishingScanResult) : PhishingUiState()
    data class Error(val message: String) : PhishingUiState()
}
