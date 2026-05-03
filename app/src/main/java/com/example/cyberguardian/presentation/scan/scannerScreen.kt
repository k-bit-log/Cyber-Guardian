package com.example.cyberguardian.presentation.scan

import android.content.Context
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyberguardian.data.ScanResult

@Composable
fun ScanScreen(
    viewModel: scannerViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    context: Context
) {
    val isScanning = viewModel.isScanning
    val progress = viewModel.progress
    val results = viewModel.results
    val threatsFound = viewModel.threatsFound

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        // Header / Status Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            ScanButton(
                isScanning = isScanning,
                onClick = { viewModel.startFullScan(context) }
            )
        }

        if (isScanning) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF2E7D32),
                trackColor = Color.DarkGray
            )
            Text(
                text = "Scanning... ${(progress * 100).toInt()}%",
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (results.isNotEmpty()) {
            ScanSummary(threatsFound = threatsFound, totalApps = results.size)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Results List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(results) { result ->
                ScanResultItem(result)
            }
        }
    }
}

@Composable
fun ScanButton(
    isScanning: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "blink")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alphaAnim"
    )

    Box(
        modifier = Modifier
            .size(140.dp)
            .clip(CircleShape)
            .background(if (isScanning) Color(0xFF1B5E20) else Color(0xFF2E7D32))
            .then(if (!isScanning) Modifier.alpha(alpha) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        if (isScanning) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier.size(70.dp)
            )
        } else {
            IconButton(
                onClick = onClick,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = "Start Scan",
                    tint = Color.White,
                    modifier = Modifier.size(70.dp)
                )
            }
        }
    }
}

@Composable
fun ScanSummary(threatsFound: Int, totalApps: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (threatsFound > 0) Color(0xFF311B1B) else Color(0xFF1B2E1B)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (threatsFound > 0) Icons.Default.Warning else Icons.Default.CheckCircle,
                contentDescription = null,
                tint = if (threatsFound > 0) Color.Red else Color.Green,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = if (threatsFound > 0) "$threatsFound Threats Found" else "Device Secure",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "Scanned $totalApps applications",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ScanResultItem(result: ScanResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (result.riskScore > 50) Color(0xFF311B1B) else Color(0xFF1B2E1B)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (result.riskScore > 50) Icons.Default.BugReport else Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (result.riskScore > 50) Color.Red else Color.Green
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = result.appName,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    if (result.isSideloaded) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = Color(0xFFFBC02D),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "APK",
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                }
                Text(
                    text = result.packageName,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Risk: ${result.riskScore}",
                    color = when {
                        result.riskScore > 70 -> Color.Red
                        result.riskScore > 30 -> Color(0xFFFBC02D)
                        else -> Color.Green
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
