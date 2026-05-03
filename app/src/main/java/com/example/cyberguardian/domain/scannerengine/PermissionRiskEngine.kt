package com.example.cyberguardian.domain.scannerengine

import android.Manifest

class PermissionRiskEngine {
    private val riskyPermissions = mapOf(
        Manifest.permission.READ_SMS to "Can read your private text messages and OTPs.",
        Manifest.permission.READ_CALL_LOG to "Can access your call history and contact details.",
        Manifest.permission.SYSTEM_ALERT_WINDOW to "Can show windows over other apps, potentially stealing data via overlays.",
        Manifest.permission.INSTALL_PACKAGES to "Can install other applications without your explicit consent.",
        Manifest.permission.WRITE_SETTINGS to "Can modify system settings, which might be used to weaken security.",
        Manifest.permission.CAMERA to "Can access your camera, potentially taking photos or videos without notice.",
        Manifest.permission.RECORD_AUDIO to "Can record audio using the microphone, potentially eavesdropping.",
        Manifest.permission.ACCESS_FINE_LOCATION to "Can track your precise physical location."
    )

    fun evaluate(permissions: Array<String>?): Pair<Int, List<String>> {
        if (permissions == null) return 0 to emptyList()

        val reasons = mutableListOf<String>()
        var score = 0

        permissions.forEach { permission ->
            riskyPermissions[permission]?.let { description ->
                score += 10
                reasons.add(description)
            }
        }

        return score to reasons
    }
}
