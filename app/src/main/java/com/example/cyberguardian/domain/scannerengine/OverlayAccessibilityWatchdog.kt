package com.example.cyberguardian.domain.scannerengine

import android.Manifest

class OverlayAccessibilityWatchdog {
    fun evaluate(permissions: Array<String>?): Pair<Int, List<String>> {
        if (permissions == null) return 0 to emptyList()

        val reasons = mutableListOf<String>()
        var score = 0

        if (permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            score += 25
            reasons.add("This app can draw windows over other applications. This is often used by banking trojans to create fake login screens (overlays) to steal your credentials.")
        }

        if (permissions.contains(Manifest.permission.BIND_ACCESSIBILITY_SERVICE)) {
            score += 30
            reasons.add("This app requests Accessibility Services. While useful for legitimate tools, malware often abuses this to 'read' your screen, capture keystrokes, and automatically grant itself further permissions.")
        }

        return score to reasons
    }
}
