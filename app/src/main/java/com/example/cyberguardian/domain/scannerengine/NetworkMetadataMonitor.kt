package com.example.cyberguardian.domain.scannerengine

import android.Manifest


//🔍 Concept:
//You cannot inspect packets — but metadata leaks intent.
//Detect:
//Continuous background connectivity
//Frequent DNS lookups
//Data transfer without foreground activity
//🧠 Technique:
//Use VpnService as a local tunnel, analyze:
//Destination frequency
//Connection timing
//App UID patterns

//⚠️ Heavy trade-offs
//VPN permission scares users
//Battery cost
//Conflicts with real VPNs

class NetworkMetadataMonitor {
    fun evaluate(permissions: Array<String>?): Pair<Int, List<String>> {

        if (permissions == null) return 0 to emptyList()

        val reasons = mutableListOf<String>()
        var score = 0

        if (permissions.contains(Manifest.permission.INTERNET)) {
            score += 5
            reasons.add("Has internet capability")
        }

        return score to reasons
    }
}