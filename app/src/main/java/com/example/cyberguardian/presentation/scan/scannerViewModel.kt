package com.example.cyberguardian.presentation.scan

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyberguardian.data.ScanResult
import com.example.cyberguardian.domain.scannerengine.ScannerEngine
import kotlinx.coroutines.delay
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

            val engine = ScannerEngine(context)
            val total = engine.size
            var processed = 0

            if (total == 0) {
                isScanning = false
                return@launch
            }

            for (app in engine) {

                val result = engine.scanApp(context, app)

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
