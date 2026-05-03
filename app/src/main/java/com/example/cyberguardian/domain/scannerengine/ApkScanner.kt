package com.example.cyberguardian.domain.scannerengine

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.example.cyberguardian.data.ScannerResultModel
import com.example.cyberguardian.presentation.scan.scannerViewModel


//🔍 Concept
//Detect known malware in: Sideloaded APK files , APKs explicitly shared with your app
//Uses: SHA-256 hash , (Optional) lightweight static inspection
//❌ What it cannot do: Scan most installed Play Store apps , Detect new or polymorphic malware
//✅ What it can do: Catch repacked trojans , Catch known spyware APKs ,Provide strong confidence when matched
//⚠️ Limitation: Hash-based detection is brittle. One byte change bypasses it.

class ApkScanner {
    fun isSideloaded(pm: PackageManager, packageName: String): Boolean {
        val installer = pm.getInstallerPackageName(packageName)
        return installer != "com.android.vending" // Not Play Store
    }

    fun isUserApp(appInfo: ApplicationInfo): Boolean {
        val isSystem = appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        val isUpdatedSystem = appInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
        return !(isSystem || isUpdatedSystem)
    }

    fun getInstalledApps(
        context: Context,
        includeSystemApps: Boolean = false
    ): List<ScannerResultModel> {

        val pm = context.packageManager
        val apps = mutableListOf<ScannerResultModel>()

        // Only apps that appear in launcher
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolvedApps = pm.queryIntentActivities(
            intent,
            PackageManager.MATCH_ALL
        )

        for (resolveInfo in resolvedApps) {

            val appInfo = resolveInfo.activityInfo.applicationInfo

            // Skip system apps unless explicitly included
            if (!includeSystemApps && isSystemApp(appInfo)) {
                continue
            }

            apps.add(
                ScannerResultModel(
                    packageName = appInfo.packageName,
                    appName = appInfo.loadLabel(pm).toString(),
                    icon = appInfo.loadIcon(pm),
                    sourceDir = appInfo.sourceDir
                )
            )
        }

        return apps.distinctBy { it.packageName }
    }

    private fun isSystemApp(appInfo: ApplicationInfo): Boolean {
        return (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
    }
//    If you want every installed package (including services, hidden apps, spyware-like background apps):
    fun getAllInstalledApps(
        context: Context,
        includeSystemApps: Boolean = false
    ): List<ScannerResultModel> {

        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        return packages
            .filter { includeSystemApps || !isSystemApp(it) }
            .map {
                ScannerResultModel(
                    packageName = it.packageName,
                    appName = it.loadLabel(pm).toString(),
                    icon = it.loadIcon(pm),
                    sourceDir = it.sourceDir
                )
            }
    }

}