package com.example.cyberguardian.domain.scannerengine

import android.Manifest

class NetworkMetadataMonitor {
    fun evaluate(permissions: Array<String>?): Pair<Int, List<String>> {
        if (permissions == null) return 0 to emptyList()

        val reasons = mutableListOf<String>()
        var score = 0

        if (permissions.contains(Manifest.permission.INTERNET)) {
            score += 5
            reasons.add("This app has permission to access the internet, which is required for it to send any collected data to a remote server.")
        }

        return score to reasons
    }
}
