package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.njwi_ficonnect.R // Make sure this points to your app's R file
import com.example.njwi_ficonnect.presentation.navigation.BottomNavigationBar// Import your bottom navigation bar

// Re-using colors
val PrimaryBlue = Color(0xFF4A90E2)
val PrimaryPurple = Color(0xFF9B59B6)
val GreenActive = Color(0xFF4CAF50) // For "High Speed", "Secure Connection"
val GraySubtle = Color(0xFFF0F2F5) // Background color

// Data class to represent a Wi-Fi package
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
    val category: String // "Hourly", "Daily", "Weekly+", "All"
)
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(
    onNavigateToConfirmPurchase: (
        packageName: String,
        packageDescription: String,
        packageDuration: String,
        packageAccess: String,
        packagePrice: Double
    ) -> Unit,
    // Callbacks for bottom navigation
    onNavigateToHome: () -> Unit,
    onNavigateToPackages: () -> Unit, // This is the current screen
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    // Dummy data for packages (replace with real data from your backend later)
    val allPackages = remember {
        listOf(
            WifiPackage(
                id = "qb1",
                name = "Quick Browse",
                description = "Perfect for quick Browse and messaging",
                price = 50.0,
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

    var selectedTab by remember { mutableStateOf("All Packages") }

    val filteredPackages = remember(selectedTab) {
        when (selectedTab) {
            "Packages", "All Packages" -> allPackages
            "Hourly" -> allPackages.filter { it.category == "Hourly" }
            "Daily" -> allPackages.filter { it.category == "Daily" }
            "Weekly+" -> allPackages.filter { it.category == "Weekly+" }
            else -> allPackages // Should not happen
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
                selectedRoute = "packages" // Indicate current selected tab
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Apply padding from Scaffold
                .background(GraySubtle)
        ) {
            // Category Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .padding(2.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TabButtonPackage(
                    text = "Packages",
                    isSelected = selectedTab == "All Packages",
                    onClick = { selectedTab = "All Packages" },
                    modifier = Modifier.weight(1f)
                )
                TabButtonPackage(
                    text = "Hourly",
                    isSelected = selectedTab == "Hourly",
                    onClick = { selectedTab = "Hourly" },
                    modifier = Modifier.weight(1f)
                )
                TabButtonPackage(
                    text = "Daily",
                    isSelected = selectedTab == "Daily",
                    onClick = { selectedTab = "Daily" },
                    modifier = Modifier.weight(1f)
                )
                TabButtonPackage(
                    text = "Weekly+",
                    isSelected = selectedTab == "Weekly+",
                    onClick = { selectedTab = "Weekly+" },
                    modifier = Modifier.weight(1f)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredPackages) { pkg ->
                    PackageCard(pkg,
                        onChoosePackageClicked = {
                            // When "Choose This Package" is clicked, navigate to ConfirmPurchaseScreen
                            onNavigateToConfirmPurchase(
                                pkg.name,
                                pkg.description,
                                pkg.duration,
                                pkg.dataAllowance, // Using dataAllowance as packageAccess
                                pkg.price
                            )
                        },
                        onPriceClicked = {
                            // NEW: When price is clicked, also navigate to ConfirmPurchaseScreen
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
                    Spacer(modifier = Modifier.height(16.dp)) // Padding at the bottom of the list
                }
            }
        }
    }
}

@Composable
fun TabButtonPackage(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(40.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = if (isSelected) Color.White else Color.Gray
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
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
            // Features (High Speed, No Setup Fee, Secure Connection, 24/7 Support)
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