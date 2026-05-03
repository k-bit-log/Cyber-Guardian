package com.example.cyberguardian.presentation.phishing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cyberguardian.data.PhishingScanResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhishingScreen(
    viewModel: PhishingViewModel = viewModel(),
    onBack: () -> Unit
) {
    var urlInput by remember { mutableStateOf("") }
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Phishing Link Detector", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = urlInput,
                onValueChange = { urlInput = it },
                label = { Text("Enter URL to scan") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF2E7D32),
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color(0xFF2E7D32),
                    unfocusedLabelColor = Color.Gray
                ),
                singleLine = true,
                trailingIcon = {
                    if (urlInput.isNotEmpty()) {
                        IconButton(onClick = { urlInput = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.Gray)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.scanUrl(urlInput) },
                modifier = Modifier.fillMaxWidth(),
                enabled = urlInput.isNotBlank() && uiState !is PhishingUiState.Loading,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                if (uiState is PhishingUiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Analyze URL")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (uiState) {
                is PhishingUiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Enter a URL and click Analyze to check for phishing.", color = Color.Gray)
                    }
                }
                is PhishingUiState.Loading -> {
                    // Handled in button, but could show a separate overlay
                }
                is PhishingUiState.Success -> {
                    PhishingResultContent(uiState.result)
                }
                is PhishingUiState.Error -> {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF311B1B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Error, contentDescription = null, tint = Color.Red)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(uiState.message, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PhishingResultContent(result: PhishingScanResult) {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (result.isMalicious) Color(0xFF311B1B) else Color(0xFF1B2E1B)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (result.isMalicious) Icons.Default.Warning else Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = if (result.isMalicious) Color.Red else Color.Green,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (result.isMalicious) "DANGEROUS LINK" else "URL APPEARS SAFE",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "VirusTotal Analysis: ${result.maliciousCount} / ${result.totalEngines} engines flagged this URL.",
                    color = Color.LightGray,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Engine Details", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(result.engineResults.filter { it.result != "clean" && it.result != "unrated" }) { engine ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(engine.engine_name, color = Color.White, fontWeight = FontWeight.Medium)
                        Text(
                            text = engine.result.uppercase(),
                            color = if (engine.category == "malicious") Color.Red else Color.Yellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
