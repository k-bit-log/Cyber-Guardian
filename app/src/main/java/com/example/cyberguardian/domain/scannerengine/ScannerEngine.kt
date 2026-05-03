package com.example.cyberguardian.domain.scannerengine

import android.content.Context
import android.content.pm.PackageManager
import com.example.cyberguardian.data.ScanResult
import com.example.cyberguardian.data.ScannerResultModel

/**
 * ScannerEngine is responsible for identifying sideloaded apps and evaluating their risk score.
 */
class ScannerEngine(private val context: Context) : Iterable<ScannerResultModel> {

    private val apkScanner = ApkScanner()
    private val permissionEngine = PermissionRiskEngine()
    private val behaviorMonitor = BehaviorMonitor()
    private val overlayWatchdog = OverlayAccessibilityWatchdog()
    private val networkMonitor = NetworkMetadataMonitor()

    private val appsToScan: List<ScannerResultModel> by lazy {
        computeInstalledApps()
    }

    val size: Int
        get() = appsToScan.size

    override fun iterator(): Iterator<ScannerResultModel> = appsToScan.iterator()

    private fun computeInstalledApps(): List<ScannerResultModel> {
        val pm = context.packageManager
        val packages = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        val results = mutableListOf<ScannerResultModel>()

        for (pkg in packages) {
            val appInfo = pkg.applicationInfo ?: continue
            
            // Skip system apps
            if (!apkScanner.isUserApp(appInfo)) continue
            
            val isSideloaded = apkScanner.isSideloaded(pm, pkg.packageName)
            
            results.add(
                ScannerResultModel(
                    appName = appInfo.loadLabel(pm).toString(),
                    packageName = pkg.packageName,
                    icon = appInfo.loadIcon(pm),
                    sourceDir = appInfo.sourceDir,
                    isSideloaded = isSideloaded
                )
            )
        }
        return results
    }

    fun getInstalledApps(): List<ScannerResultModel> = appsToScan

    suspend fun scanApp(context: Context, app: ScannerResultModel): ScanResult {
        val threats = mutableListOf<String>()
        var score = 0

        try {
            val pm = context.packageManager
            val pkgInfo = pm.getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS)
            val appInfo = pkgInfo.applicationInfo
            val permissions = pkgInfo.requestedPermissions

            // Evaluate different risk factors using instance engines
            val (pScore, pReasons) = permissionEngine.evaluate(permissions)
            score += pScore
            threats.addAll(pReasons)

            if (appInfo != null) {
                val (bScore, bReasons) = behaviorMonitor.evaluate(appInfo)
                score += bScore
                threats.addAll(bReasons)
            }

            val (oScore, oReasons) = overlayWatchdog.evaluate(permissions)
            score += oScore
            threats.addAll(oReasons)

            val (nScore, nReasons) = networkMonitor.evaluate(permissions)
            score += nScore
            threats.addAll(nReasons)

        } catch (e: Exception) {
            threats.add("Error scanning package: ${e.message}")
        }

        return ScanResult(
            packageName = app.packageName,
            appName = app.appName,
            riskScore = score.coerceAtMost(100),
            threats = threats,
            isSideloaded = app.isSideloaded
        )
    }
}
