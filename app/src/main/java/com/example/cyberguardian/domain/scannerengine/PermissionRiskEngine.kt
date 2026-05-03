package com.example.cyberguardian.domain.scannerengine

import android.Manifest


//🔍 Concept: Permissions don’t equal malware, but certain combinations strongly correlate with abuse.
//🧠 Risk model (example)
//Pattern	                                    Risk
//READ_SMS + INTERNET	                        High
//ACCESSIBILITY + INTERNET	                    Critical
//SYSTEM_ALERT_WINDOW + BANK APP FOREGROUND	    Critical
//BOOT_COMPLETED + WAKE_LOCK	                Medium
//⚠️ Counterpoint: Accessibility is heavily used by legit apps — false positives are inevitable.


class PermissionRiskEngine {
    private val riskyPermissions = setOf(
        Manifest.permission.READ_SMS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.SYSTEM_ALERT_WINDOW,
        Manifest.permission.INSTALL_PACKAGES,
        Manifest.permission.WRITE_SETTINGS,
        Manifest.permission.CAMERA
    )

    fun evaluate(permissions: Array<String>?): Pair<Int, List<String>> {

        if (permissions == null) return 0 to emptyList()

        val reasons = mutableListOf<String>()
        var score = 0

        permissions.forEach {
            if (riskyPermissions.contains(it)) {
                score += 10
                reasons.add("Uses risky permission: $it")
            }
        }

        return score to reasons
    }
}


// Prev Code

//    data class RiskResult(
//        val score: Int,
//        val reasons: List<String>
//    )
//
//    fun evaluate(permissions: List<String>): RiskResult {
//        var score = 0
//        val reasons = mutableListOf<String>()
//
//        if (
//            permissions.contains(Manifest.permission.READ_SMS) &&
//            permissions.contains(Manifest.permission.INTERNET)
//        ) {
//            score += 30
//            reasons.add("Can read SMS and send data online")
//        }
//
//        if (permissions.contains(Manifest.permission.BIND_ACCESSIBILITY_SERVICE)) {
//            score += 50
//            reasons.add("Accessibility access enables spyware behavior")
//        }
//
//        if (permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
//            score += 20
//            reasons.add("Overlay permission can be used for phishing")
//        }
//
//        return RiskResult(score.coerceAtMost(100), reasons)
//    }
