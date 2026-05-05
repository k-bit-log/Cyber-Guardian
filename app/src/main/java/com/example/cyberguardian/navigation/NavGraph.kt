package com.example.cyberguardian.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cyberguardian.presentation.awareness.AwarenessScreen
import com.example.cyberguardian.presentation.dashboard.DashboardScreen
import com.example.cyberguardian.presentation.phishing.PhishingScreen
import com.example.cyberguardian.presentation.scan.ScanScreen
import com.example.cyberguardian.presentation.scan.scannerViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val scannerViewModel: scannerViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        containerColor = Color(0xFF0A0A0A),
        topBar = {
            // FIXED GLOBAL TOP BAR - Shared across all screens
            Surface(color = Color(0xFF0A0A0A)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1B2E1B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFF2E7D32))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Cyber Guardian",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Stay safe, always",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings", tint = Color.Gray)
                    }
                }
            }
        },
        bottomBar = {
            // FIXED GLOBAL BOTTOM BAR - Shared across all screens
            NavigationBar(
                containerColor = Color(0xFF0A0A0A),
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    Triple("dashboard", "Home", Icons.Default.Home),
                    Triple("phishing_detector", "Phishing", Icons.Default.Link),
                    Triple("awareness", "Awareness", Icons.Default.School)
                )
                items.forEach { (route, label, icon) ->
                    // Selection logic: Highlight Home tab when on Dashboard or Malware Scanner result screen
                    val isHomeTab = route == "dashboard"
                    val isSelected = currentDestination?.hierarchy?.any { it.route == route } == true ||
                            (isHomeTab && currentDestination?.route == "malware_scanner")

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            // Standard Navigation: popping up to start destination ensures 
                            // we return to Dashboard root even if we are on Malware Scanner.
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF4CAF50),
                            selectedTextColor = Color(0xFF4CAF50),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(padding)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    scannerViewModel = scannerViewModel,
                    onNavigateToScanner = { navController.navigate("malware_scanner") },
                    onNavigateToPhishing = { navController.navigate("phishing_detector") },
                    onNavigateToAwareness = { navController.navigate("awareness") }
                )
            }

            composable("malware_scanner") {
                ScanScreen(viewModel = scannerViewModel)
            }

            composable("phishing_detector") {
                PhishingScreen()
            }

            composable("awareness") {
                AwarenessScreen()
            }
        }
    }
}
