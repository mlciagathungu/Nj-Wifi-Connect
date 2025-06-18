package com.example.njwi_ficonnect.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.njwi_ficonnect.R
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import com.example.njwi_ficonnect.presentation.navigation.BottomNavigationBar
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Color constants (move to a common theme file if needed)
val PrimaryB = Color(0xFF0694EC)
val PrimaryP = Color(0xFF9419A4)
val OrangeAccent = Color(0xFFFF9800)
val GreenAccent = Color(0xFF4CAF50)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "User",
    onBrowsePackagesClicked: () -> Unit = {},
    onMySubscriptionsClicked: () -> Unit = {},
    onAccountSettingsClicked: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToPackages: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    selectedRoute: String = "home"
) {
    // State for live time and date
    var currentTime by remember { mutableStateOf(LocalDateTime.now()) }

    // Update time every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now()
            delay(1000)
        }
    }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    val dateFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
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
                            Text(
                                text = currentTime.format(timeFormatter),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryP
                            )
                            Text(
                                text = currentTime.format(dateFormatter),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onNavigateToHome = onNavigateToHome,
                onNavigateToPackages = onNavigateToPackages,
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToProfile = onNavigateToProfile,
                selectedRoute = selectedRoute
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF1F3F1))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                // Welcome Card
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
                            painter = painterResource(id = R.drawable.ic_wifi_logo),
                            contentDescription = "Wi-Fi Icon",
                            tint = PrimaryB,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No Active Connection",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
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
            }
            item {
                // Statistics row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatCard(
                        value = "2,847",
                        label = "Active Users",
                        icon = painterResource(id = R.drawable.ic_users),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatCard(
                        value = "156",
                        label = "Hotspots",
                        icon = painterResource(id = R.drawable.ic_hotspot),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatCard(
                        value = "99.9%",
                        label = "Uptime",
                        icon = painterResource(id = R.drawable.ic_uptime),
                        modifier = Modifier.weight(1f),
                        valueColor = OrangeAccent
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                Text(
                    text = "Quick Actions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                QuickActionItem(
                    icon = painterResource(id = R.drawable.ic_browse_packages),
                    title = "Browse Packages",
                    description = "Find the perfect plan",
                    onClick = onBrowsePackagesClicked
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                QuickActionItem(
                    icon = painterResource(id = R.drawable.ic_subscriptions),
                    title = "My Subscriptions",
                    description = "View history & active plans",
                    onClick = onMySubscriptionsClicked
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                QuickActionItem(
                    icon = painterResource(id = R.drawable.ic_profile),
                    title = "Account Settings",
                    description = "Manage your profile",
                    onClick = onAccountSettingsClicked
                )
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                Text(
                    text = "Why Choose NJ Connect?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            item {
                WhyChooseItem(
                    icon = painterResource(id = R.drawable.ic_lightning_fast),
                    title = "Lightning Fast",
                    description = "High-speed internet for all your needs"
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                WhyChooseItem(
                    icon = painterResource(id = R.drawable.ic_secure_connection),
                    title = "Secure Connection",
                    description = "Enterprise-grade security protocols",
                    iconTint = GreenAccent
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                WhyChooseItem(
                    icon = painterResource(id = R.drawable.ic_support),
                    title = "24/7 Support",
                    description = "Round-the-clock customer assistance"
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    valueColor: Color = PrimaryP
) {
    Card(
        modifier = modifier.height(100.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFCAEAA9)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = PrimaryB,
                modifier = Modifier.size(28.dp)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = value,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = label,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun QuickActionItem(
    icon: Painter,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = PrimaryB,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(description, fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun WhyChooseItem(
    icon: Painter,
    title: String,
    description: String,
    iconTint: Color = PrimaryB
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = title,
            tint = iconTint,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(description, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        userName = "Mlcia",
        onBrowsePackagesClicked = {},
        onMySubscriptionsClicked = {},
        onAccountSettingsClicked = {},
        onNavigateToHome = {},
        onNavigateToPackages = {},
        onNavigateToHistory = {},
        onNavigateToProfile = {},
        selectedRoute = "home",
    )
}