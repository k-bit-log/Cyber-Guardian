package com.example.cyberguardian.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cyberguardian.presentation.awareness.AwarenessScreen
import com.example.cyberguardian.presentation.phishing.PhishingScreen
import com.example.cyberguardian.presentation.scan.ScanScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "malware_scanner"
    ) {
        composable("malware_scanner") {
            ScanScreen(
                context = LocalContext.current,
                onNavigateToPhishing = { navController.navigate("phishing_detector") },
                onNavigateToAwareness = { navController.navigate("awareness") }
            )
        }

        composable("phishing_detector") {
            PhishingScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable("awareness") {
            AwarenessScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
