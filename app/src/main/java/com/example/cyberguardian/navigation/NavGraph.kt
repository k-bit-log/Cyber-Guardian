package com.example.cyberguardian.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cyberguardian.presentation.scan.ScanScreen


@Composable
fun NavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "malware_scanner"
    ) {

//        composable("dashboard") {
//            DashboardScreen()
//        }

        composable("malware_scanner") {
            ScanScreen(Context)
        }
    }
}