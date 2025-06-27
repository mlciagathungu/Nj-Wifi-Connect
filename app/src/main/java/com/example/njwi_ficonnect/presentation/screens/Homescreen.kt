package com.example.njwi_ficonnect.presentation.screens

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
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.njwi_ficonnect.presentation.navigation.BottomNavigationBar
import com.example.njwi_ficonnect.presentation.viewmodel.PackageHistoryViewModel
import com.example.njwi_ficonnect.presentation.viewmodel.ProfileViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

// Color constants (move to a common theme file if needed)
val PrimaryB = Color(0xFF0694EC)
val PrimaryP = Color(0xFF9419A4)
val OrangeAccent = Color(0xFFFF9800)
val GreenAccent = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    profileViewModel: ProfileViewModel = viewModel(), // 👈 Add this
    packageHistoryViewModel: PackageHistoryViewModel,
    onBrowsePackagesClicked: () -> Unit = {},
    onMySubscriptionsClicked: () -> Unit = {},
    onAccountSettingsClicked: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToPackages: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    selectedRoute: String = "home",
    hasActiveSubscription: Boolean = false,
    activePackageName: String = "",
    activePackageExpiry: Long = 0L,
) {val activeCount = packageHistoryViewModel.activeCount
    val expiredCount = packageHistoryViewModel.expiredCount
    val isLoading = packageHistoryViewModel.isLoading
    val errorMessage = packageHistoryViewModel.errorMessage



    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    // Update time every second
    LaunchedEffect(Unit) {
        while (true) {
            currentTimeMillis = System.currentTimeMillis()
            delay(1000)
        }
    }

    val dateObj = Date(currentTimeMillis)
    val timeText = remember(dateObj) {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(dateObj)
    }
    val dateText = remember(dateObj) {
        SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(dateObj)
    }
    val displayName = profileViewModel.userProfile.getDisplayName()


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
                                text = "Welcome back, $displayName",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = if (hasActiveSubscription)
                                    "Your package: $activePackageName"
                                else
                                    "Ready to get connected?",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = timeText,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryP
                            )
                            Text(
                                text = dateText,
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
                // Welcome Card (shows active subscription if present)
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
                        if (hasActiveSubscription) {
                            Text(
                                text = "Connected: $activePackageName",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenAccent
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Expires: ${
                                    SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault())
                                        .format(Date(activePackageExpiry))
                                }",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        } else {
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
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onBrowsePackagesClicked,
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (hasActiveSubscription) GreenAccent else PrimaryB
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                if (hasActiveSubscription) "Renew/Change Package" else "Browse Packages",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    text = "My Activation Summary",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Active Packages: $activeCount", color = GreenAccent, fontSize = 16.sp)
                        Text("Expired Packages: $expiredCount", color = Color.Gray, fontSize = 16.sp)
                    }
                }
            }

            item {
                Button(
                    onClick = { packageHistoryViewModel.refresh() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Refresh Activation History")
                }
            }

            if (isLoading) {
                item {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }

            errorMessage?.let { error ->
                item {
                    Text(
                        text = "Error: $error",
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
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

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        onBrowsePackagesClicked = {},
        onMySubscriptionsClicked = {},
        onAccountSettingsClicked = {},
        onNavigateToHome = {},
        onNavigateToPackages = {},
        onNavigateToHistory = {},
        onNavigateToProfile = {},
        selectedRoute = "home",
        hasActiveSubscription = true,
        activePackageName = "Business Package",
        activePackageExpiry = System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000,
        packageHistoryViewModel = TODO() // 3 days from now
    )
}