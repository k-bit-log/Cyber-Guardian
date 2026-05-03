package com.example.cyberguardian.data

import android.graphics.drawable.Drawable

data class ScannerResultModel(
    val appName: String,
    val packageName: String,
    val icon: Drawable?,
    val sourceDir: String,
    val isSideloaded: Boolean = false
)


data class ScanResult(
    val packageName: String,
    val appName: String,
    val riskScore: Int,      // 0–100
    val threats: List<String>,
    val isSideloaded: Boolean = false
)
