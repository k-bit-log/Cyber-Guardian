package com.example.cyberguardian.domain.scannerengine

import android.content.pm.ApplicationInfo

class BehaviorMonitor {
    fun evaluate(appInfo: ApplicationInfo): Pair<Int, List<String>> {
        val reasons = mutableListOf<String>()
        var score = 0

        // Heuristic: app runs in background capability
        if (!appInfo.enabled) {
            score += 5
            reasons.add("This app is currently disabled but still present on your device, which is unusual for active malware but could be a dormant threat.")
        }
        
        // You could add more heuristic checks here, for example:
        // if (appInfo.flags and ApplicationInfo.FLAG_PERSISTENT != 0) { ... }

        return score to reasons
    }
}