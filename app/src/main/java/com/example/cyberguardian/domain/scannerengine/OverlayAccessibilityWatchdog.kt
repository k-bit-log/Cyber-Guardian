package com.example.cyberguardian.domain.scannerengine

import android.Manifest
import android.content.Context


//🔍 Concept
//Most Android malware today: Abuses Accessibility, Draws overlays, Targets banking / OTP / UPI apps
//🧠 Detection logic :
// If: Overlay permission granted, Accessibility service enabled ,Overlay appears while sensitive app is foreground
//→ Very high confidence threat
//⚠️ Counterargument:
//Accessibility misuse detection is probabilistic
//Legit screen readers look dangerous on paper


class OverlayAccessibilityWatchdog {
    fun evaluate(permissions: Array<String>?): Pair<Int, List<String>> {

        if (permissions == null) return 0 to emptyList()

        val reasons = mutableListOf<String>()
        var score = 0

        if (permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            score += 25
            reasons.add("Can draw over other apps (overlay)")
        }

        if (permissions.contains(Manifest.permission.BIND_ACCESSIBILITY_SERVICE)) {
            score += 30
            reasons.add("Uses accessibility service")
        }

        return score to reasons
    }
}