package com.example.cyberguardian.domain.scannerengine

import android.annotation.SuppressLint
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo


//🔍 Concept: Malware reveals itself when running, not when installed.
//This module listens for: App launches , Background persistence , Suspicious lifecycle patterns
//🧩 Signals you can observe : App starts without UI ,Relaunch after force-stop, Abnormal foreground/background switching
//⚠️ Limitations : Requires Usage Access (user friction) , OEM ROMs may lie or restrict data ,No visibility into what the app does internally


class BehaviorMonitor {
    fun evaluate(appInfo: ApplicationInfo): Pair<Int, List<String>> {

        val reasons = mutableListOf<String>()
        var score = 0

        // Heuristic: app runs in background capability
        if (!appInfo.enabled) {
            score += 5
            reasons.add("App disabled but present")
        }

        return score to reasons
    }
}