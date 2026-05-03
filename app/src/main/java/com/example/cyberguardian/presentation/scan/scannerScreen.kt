package com.example.cyberguardian.presentation.scan

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyberguardian.data.ScannerResultModel

@Composable
fun ScanScreen(
    viewModel: scannerViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    context: Context
) {
    val isScanning by viewModel.isScanning.collectAsState()

    // Blinking animation
    val infiniteTransition = rememberInfiniteTransition(label = "blink")

    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaAnim"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    if (isScanning) Color(0xFF1B5E20) else Color(0xFF2E7D32)
                ),
            contentAlignment = Alignment.Center
        ) {

            if (isScanning) {

                // Show loading circular indicator
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(60.dp)
                )

            } else {

                IconButton(
                    onClick = {viewModel.startScan(context)},
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alpha) // blinking effect
                ) {
                    Icon(
                        imageVector = Icons.Default.PowerSettingsNew,
                        contentDescription = "Start Scan",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}


//
//@Composable
//fun ThreatSummary(viewModel: ScannerResultModel) {
//
//    Card(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Column(Modifier.padding(16.dp)) {
//
//            Text(
//                text = "Threats Found: ${viewModel.threatsFound}",
//                style = MaterialTheme.typography.titleMedium
//            )
//
//            if (!viewModel.isScanning && viewModel.results.isNotEmpty()) {
//
//                Text(
//                    text = if (viewModel.threatsFound == 0)
//                        "Device appears safe"
//                    else
//                        "Potential risks detected",
//                    color = if (viewModel.threatsFound == 0)
//                        Color(0xFF2E7D32)
//                    else
//                        Color.Red
//                )
//            }
//        }
//    }
//}

//@Composable
//fun ResultList(viewModel: ScannerResultModel) {
//
//    LazyColumn {
//
//        items(viewModel.results) { result ->
//
//            ResultItem(result)
//        }
//    }
//}

