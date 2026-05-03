package com.example.cyberguardian.presentation.scan

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyberguardian.data.ScanResult
import com.example.cyberguardian.data.ScannerResultModel
import com.example.cyberguardian.domain.scannerengine.ScannerEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class scannerViewModel() : ViewModel() {
    // Scan state
    var isScanning by mutableStateOf(false)
        private set
    var progress by mutableStateOf(0f)
        private set
    // Results
    val results = mutableStateListOf<ScanResult>()
    var threatsFound by mutableStateOf(0)
        private set

    fun startFullScan(context: Context) {

        if (isScanning) return

        isScanning = true
        progress = 0f
        threatsFound = 0
        results.clear()

        viewModelScope.launch {

            val apps = ScannerEngine(context)
            val total = apps.size
            var processed = 0

            for (app in apps) {

                val result = ScannerEngine.scanApp(context, app)

                results.add(result)

                if (result.riskScore >= 60)
                    threatsFound++

                processed++
                progress = processed.toFloat() / total

                delay(40) // smooth animation
            }

            isScanning = false
        }
    }

}