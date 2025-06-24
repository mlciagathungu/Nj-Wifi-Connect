package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(onBack: () -> Unit) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Help & Support") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Need assistance? We're here to help!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                "• For FAQs, troubleshooting, and common issues, visit our online Help Center.\n" +
                        "• For urgent matters, contact support directly via email or WhatsApp.",
                fontSize = 16.sp
            )

            Button(
                onClick = {
                    uriHandler.openUri("mailto:support@njconnect.co.ke?subject=Nj-WiFi-Connect%20Support")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Contact Support via Email")
            }

            Button(
                onClick = {
                    uriHandler.openUri("https://wa.me/254712345678?text=Hello%20NJ%20WiFi%20Support")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
            ) {
                Text("Contact Support via WhatsApp", color = Color.White)
            }

            Button(
                onClick = {
                    uriHandler.openUri("https://www.njconnect.co.ke/help")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Visit Online Help Center", color = MaterialTheme.colorScheme.onSecondary)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Our team will get back to you as soon as possible.\n\nThank you for using NJ Connect Wi-Fi!",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}