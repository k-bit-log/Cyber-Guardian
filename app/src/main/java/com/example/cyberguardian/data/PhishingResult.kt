package com.example.cyberguardian.data

data class VirusTotalResponse(
    val data: VirusTotalData
)

data class VirusTotalData(
    val id: String,
    val type: String,
    val attributes: VirusTotalAttributes
)

data class VirusTotalAttributes(
    val last_analysis_stats: AnalysisStats,
    val last_analysis_results: Map<String, AnalysisResult>
)

data class AnalysisStats(
    val harmless: Int,
    val malicious: Int,
    val suspicious: Int,
    val timeout: Int,
    val undetected: Int
)

data class AnalysisResult(
    val method: String,
    val engine_name: String,
    val category: String,
    val result: String
)

data class PhishingScanResult(
    val url: String,
    val isMalicious: Boolean,
    val maliciousCount: Int,
    val totalEngines: Int,
    val engineResults: List<AnalysisResult>
)
