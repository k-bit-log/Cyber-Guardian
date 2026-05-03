package com.example.cyberguardian.presentation.awareness

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

data class AwarenessItem(
    val title: String,
    val description: String,
    val youtubeUrl: String? = null
)

val awarenessContent = listOf(
    AwarenessItem(
        "Phishing Attacks",
        "Phishing is a method of trying to gather personal information using deceptive emails and websites.",
        "https://www.youtube.com/watch?v=XBkzBrXllP0"
    ),
    AwarenessItem(
        "Strong Passwords",
        "Learn how to create and manage strong, unique passwords for all your accounts.",
        "https://www.youtube.com/watch?v=qMTrJ_W9_Dk"
    ),
    AwarenessItem(
        "Two-Factor Authentication (2FA)",
        "2FA adds an extra layer of security to your online accounts.",
        "https://www.youtube.com/watch?v=0mvCeNsTa1g"
    ),
    AwarenessItem(
        "Social Engineering",
        "Understanding how attackers manipulate people into giving up confidential information.",
        "https://www.youtube.com/watch?v=lc7scxvKQOo"
    ),
    AwarenessItem(
        "Mobile Security Tips",
        "Keep your smartphone safe from malware and data breaches with these simple steps.",
        "https://www.youtube.com/watch?v=Z5S6vE_V_lU"
    ),
    AwarenessItem(
        "Public Wi-Fi Risks",
        "Using public Wi-Fi can expose your data to hackers. Learn how to stay protected.",
        "https://www.youtube.com/watch?v=9M66r9A1VqE"
    ),
    AwarenessItem(
        "Ransomware Protection",
        "What is ransomware and how can you prevent your files from being held hostage?",
        "https://www.youtube.com/watch?v=W3Q_B46S868"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AwarenessScreen(onBack: () -> Unit) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cyber Awareness", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Stay Safe Online",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Education is the first line of defense against cyber threats.",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(awarenessContent) { item ->
                AwarenessCard(item) {
                    item.youtubeUrl?.let { url ->
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun AwarenessCard(item: AwarenessItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = item.youtubeUrl != null, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                if (item.youtubeUrl != null) {
                    Icon(
                        imageVector = Icons.Default.PlayCircle,
                        contentDescription = "Watch Video",
                        tint = Color.Red
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.description,
                color = Color.LightGray,
                fontSize = 14.sp
            )
            if (item.youtubeUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "WATCH VIDEO",
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
