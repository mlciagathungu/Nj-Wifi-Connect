package com.example.njwi_ficonnect.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Re-using colors
val PrimaryBlue = Color(0xFF4A90E2)
val PrimaryPurple = Color(0xFF9B59B6)
val OrangeAccent = Color(0xFFFFA726)
val GreenAccent = Color(0xFF66BB6A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "Micia Gathungu!",
    onBrowsePackagesClicked: () -> Unit,
    onMySubscriptionsClicked: () -> Unit,
    onAccountSettingsClicked: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Welcome back, $userName",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "Ready to get connected?",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "17:58", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryPurple)
                            Text(text = "6/8/2025", fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            )
        },
        bottomBar = {
            PreviewBottomNavigationBar()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F2F5))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Wi-Fi Icon",
                        tint = PrimaryBlue,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No Active Connection",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Choose a package to get started",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onBrowsePackagesClicked,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Browse Packages", color = Color.White, fontSize = 16.sp)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                StatCard(
                    value = "2,847",
                    label = "Active Users",
                    icon = Icons.Default.Build,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(
                    value = "156",
                    label = "Hotspots",
                    icon = Icons.Default.Build,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatCard(
                    value = "99.9%",
                    label = "Uptime",
                    icon = Icons.Default.Build,
                    valueColor = OrangeAccent,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Quick Actions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            QuickActionItem(
                icon = Icons.Default.Build,
                title = "Browse Packages",
                description = "Find the perfect plan",
                onClick = onBrowsePackagesClicked
            )
            Spacer(modifier = Modifier.height(8.dp))
            QuickActionItem(
                icon = Icons.Default.Build,
                title = "My Subscriptions",
                description = "View history & active plans",
                onClick = onMySubscriptionsClicked
            )
            Spacer(modifier = Modifier.height(8.dp))
            QuickActionItem(
                icon = Icons.Default.Settings,
                title = "Account Settings",
                description = "Manage your profile",
                onClick = onAccountSettingsClicked
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Why Choose NJ Connect?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            WhyChooseItem(
                icon = Icons.Default.Build,
                title = "Lightning Fast",
                description = "High-speed internet for all your needs"
            )
            Spacer(modifier = Modifier.height(8.dp))
            WhyChooseItem(
                icon = Icons.Default.Build,
                title = "Secure Connection",
                description = "Enterprise-grade security protocols",
                iconTint = GreenAccent
            )
            Spacer(modifier = Modifier.height(8.dp))
            WhyChooseItem(
                icon = Icons.Default.Build,
                title = "24/7 Support",
                description = "Round-the-clock customer assistance"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Black
) {
    Card(
        modifier = modifier.height(90.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PrimaryBlue,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = valueColor)
            Text(label, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(10.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = PrimaryBlue, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Navigate", tint = Color.LightGray)
        }
    }
}

@Composable
fun WhyChooseItem(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    iconTint: Color = PrimaryBlue
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

// Minimal preview stubs
@Composable
fun PreviewBottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) {
            Icon(imageVector = Icons.Default.Build, contentDescription = "Nav", tint = PrimaryPurple)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        onBrowsePackagesClicked = { /* preview */ },
        onMySubscriptionsClicked = { /* preview */ },
        onAccountSettingsClicked = { /* preview */ },
        onNavigateToPackages = { /* preview */ },
        onNavigateToHistory = { /* preview */ },
        onNavigateToProfile = { /* preview */ }
    )
}