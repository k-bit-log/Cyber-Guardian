package com.example.cyberguardian.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyberguardian.presentation.scan.scannerViewModel

@Composable
fun DashboardScreen(
    scannerViewModel: scannerViewModel,
    onNavigateToScanner: () -> Unit,
    onNavigateToPhishing: () -> Unit,
    onNavigateToAwareness: () -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val threatsFound = scannerViewModel.threatsFound
    val results = scannerViewModel.results
    val totalApps = results.size
    val isSecure = threatsFound == 0
    val isScanning = scannerViewModel.isScanning

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Protection Status Circle
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                if (isSecure) Color(0xFF1B5E20).copy(alpha = 0.3f) else Color(0xFFB71C1C).copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(if (isSecure) Color(0xFF1B2E1B) else Color(0xFF311B1B)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isScanning) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 4.dp)
                    } else {
                        Icon(
                            if (isSecure) Icons.Default.Check else Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isScanning) "Scanning..." else if (isSecure) "You're Protected" else "Threats Detected",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isScanning) "${(scannerViewModel.progress * 100).toInt()}% completed" else if (isSecure) "No active threats" else "$threatsFound issues found",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 1. Scan Button Card - Triggers scan
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isScanning) { scannerViewModel.startFullScan(context) },
            colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(if (isScanning) "Scanning..." else "Scan", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Tap to run security scan", color = Color.Gray, fontSize = 13.sp)
                }
                if (isScanning) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = Color(0xFF4CAF50))
                } else {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Result Overview Card - Directly below Scan btn and navigates to details
        if (totalApps > 0 || isScanning) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToScanner() },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatItem(Icons.Default.Shield, totalApps.toString(), "Apps Scanned")
                    StatItem(Icons.Default.Warning, threatsFound.toString(), "Threats Found", iconColor = if (threatsFound > 0) Color.Red else Color.Gray)
                    StatItem(
                        if (isSecure) Icons.Default.CheckCircle else Icons.Default.Warning,
                        if (isSecure) "Protected" else "At Risk",
                        "Real-time",
                        iconColor = if (isSecure) Color(0xFF4CAF50) else Color.Red
                    )
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.DarkGray, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Security Tools", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("3 Modules", color = Color(0xFF2E7D32), fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Modules
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SecurityModuleCard(
                icon = Icons.Default.Shield,
                iconBg = Color(0xFF1B2E1B),
                iconColor = Color(0xFF4CAF50),
                title = "Scanner",
                description = "Detect and remove threats from your apps",
                statusText = if (isSecure) "Safe" else "Risk",
                statusColor = if (isSecure) Color(0xFF4CAF50) else Color.Red,
                onClick = onNavigateToScanner
            )

            SecurityModuleCard(
                icon = Icons.Default.Language,
                iconBg = Color(0xFF1A237E).copy(alpha = 0.2f),
                iconColor = Color(0xFF42A5F5),
                title = "Phishing Shield",
                description = "Detect dangerous links before you click",
                statusText = "Active",
                statusColor = Color(0xFF42A5F5),
                onClick = onNavigateToPhishing
            )

            SecurityModuleCard(
                icon = Icons.Default.School,
                iconBg = Color(0xFF4A148C).copy(alpha = 0.2f),
                iconColor = Color(0xFFAB47BC),
                title = "Awareness",
                description = "Learn and stay informed about digital safety",
                statusText = "Explore",
                statusColor = Color(0xFFAB47BC),
                onClick = onNavigateToAwareness
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SecurityModuleCard(
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    title: String,
    description: String,
    statusText: String,
    statusColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF161616)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(description, color = Color.Gray, fontSize = 12.sp, maxLines = 1)
            }
            Surface(
                color = statusColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        if (statusText == "Safe" || statusText == "Active") Icons.Default.CheckCircle else if (statusText == "Risk") Icons.Default.Warning else Icons.Default.Book,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(statusText, color = statusColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.DarkGray)
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, value: String, label: String, iconColor: Color = Color.Gray) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(value, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Color.Gray, fontSize = 10.sp)
        }
    }
}
