package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.njwi_ficonnect.R
import com.example.njwi_ficonnect.presentation.navigation.BottomNavigationBar

val PrimaryBlue = Color(0xFF4A90E2)
val PrimaryPurple = Color(0xFF9B59B6)
val GreenActive = Color(0xFF4CAF50)
val GraySubtle = Color(0xFFF0F2F5)

data class WifiPackage(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val duration: String,
    val dataAllowance: String,
    val hasHighSpeed: Boolean,
    val hasNoSetupFee: Boolean,
    val hasSecureConnection: Boolean,
    val has24_7Support: Boolean,
    val isMostPopular: Boolean = false,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(
    onNavigateToConfirmPurchase: (
        packageName: String,
        packageDescription: String,
        packageDuration: String,
        packageAccess: String,
        packagePrice: Double
    ) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    // TODO: Replace with real repository in production!
    val allPackages = remember {
        listOf(
            WifiPackage(
                id = "qb1",
                name = "Quick Browse",
                description = "Perfect for quick Browse and messaging",
                price = 1.0,
                duration = "2h",
                dataAllowance = "0.5GB",
                hasHighSpeed = true,
                hasNoSetupFee = true,
                hasSecureConnection = true,
                has24_7Support = true,
                category = "Hourly"
            ),
            WifiPackage(
                id = "de1",
                name = "Daily Essential",
                description = "Great for work and social media",
                price = 150.0,
                duration = "1d",
                dataAllowance = "2.0GB",
                hasHighSpeed = true,
                hasNoSetupFee = true,
                hasSecureConnection = true,
                has24_7Support = true,
                isMostPopular = true,
                category = "Daily"
            ),
            WifiPackage(
                id = "pu1",
                name = "Power User",
                description = "Unlimited data for heavy usage",
                price = 400.0,
                duration = "1d",
                dataAllowance = "Unlimited",
                hasHighSpeed = true,
                hasNoSetupFee = true,
                hasSecureConnection = true,
                has24_7Support = true,
                category = "Daily"
            ),
            WifiPackage(
                id = "wb1",
                name = "Weekly Bundle",
                description = "Unlimited data for heavy usage throughout the week",
                price = 800.0,
                duration = "7d",
                dataAllowance = "Unlimited",
                hasHighSpeed = true,
                hasNoSetupFee = true,
                hasSecureConnection = true,
                has24_7Support = true,
                isMostPopular = true,
                category = "Weekly+"
            )
        )
    }

    val tabs = listOf("All", "Hourly", "Daily", "Weekly+")
    var selectedTabIndex by remember { mutableStateOf(0) }
    val selectedTab = tabs[selectedTabIndex]

    val filteredPackages = remember(selectedTab, allPackages) {
        when (selectedTab) {
            "All" -> allPackages
            else -> allPackages.filter { it.category == selectedTab }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Packages", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB8ECA9)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onNavigateToHome = onNavigateToHome,
                onNavigateToPackages = onNavigateToPackages,
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToProfile = onNavigateToProfile,
                selectedRoute = "packages"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(GraySubtle)
        ) {
            // TabRow for package categories
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(10.dp)),
                containerColor = Color.White,
                contentColor = PrimaryBlue
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = if (selectedTabIndex == index) PrimaryBlue else Color.Gray
                            )
                        }
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredPackages) { pkg ->
                    PackageCard(pkg,
                        onChoosePackageClicked = {
                            onNavigateToConfirmPurchase(
                                pkg.name,
                                pkg.description,
                                pkg.duration,
                                pkg.dataAllowance,
                                pkg.price
                            )
                        },
                        onPriceClicked = {
                            onNavigateToConfirmPurchase(
                                pkg.name,
                                pkg.description,
                                pkg.duration,
                                pkg.dataAllowance,
                                pkg.price
                            )
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun PackageCard(
    pkg: WifiPackage,
    onChoosePackageClicked: () -> Unit,
    onPriceClicked: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFB8ECA9)
        )
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    if (pkg.isMostPopular) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Most Popular",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Most Popular",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = PrimaryBlue
                            )
                        }
                    }
                    Text(
                        text = pkg.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                Text(
                    text = "KSh ${pkg.price.toInt()}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.clickable { onPriceClicked() }
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = pkg.description,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_duration),
                    contentDescription = "Duration",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = pkg.duration, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_data_allowance),
                    contentDescription = "Data Allowance",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = pkg.dataAllowance, fontSize = 14.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                if (pkg.hasHighSpeed) FeatureRow("High Speed")
                if (pkg.hasNoSetupFee) FeatureRow("No Setup Fee")
                if (pkg.hasSecureConnection) FeatureRow("Secure Connection")
                if (pkg.has24_7Support) FeatureRow("24/7 Support")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = onChoosePackageClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = if (pkg.isMostPopular) Brush.horizontalGradient(
                                colors = listOf(PrimaryPurple, PrimaryBlue)
                            ) else Brush.horizontalGradient(
                                colors = listOf(PrimaryBlue, PrimaryPurple)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Choose This Package", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun FeatureRow(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_dot),
            contentDescription = null,
            tint = GreenActive,
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, fontSize = 13.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPackagesScreen() {
    PackagesScreen(
        onNavigateToConfirmPurchase = { _, _, _, _, _ -> },
        onNavigateToHome = { /* preview */ },
        onNavigateToPackages = { /* preview */ },
        onNavigateToHistory = { /* preview */ },
        onNavigateToProfile = { /* preview */ }
    )
}