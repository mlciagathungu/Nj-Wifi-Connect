package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.njwi_ficonnect.R
import com.example.njwi_ficonnect.presentation.navigation.BottomNavigationBar

// Custom color names for clarity
val HistoryPrimaryBlue = Color(0xFF56F804)
val HistoryPrimaryPurple = Color(0xFFD2F107)
val HistoryStatsGreen = Color(0xFFE8F5E9)
val HistoryStatsBlue = Color(0xFFE3F2FD)
val HistoryBackground = Color(0xFFF0F2F5)
val HistoryTextGray = Color(0xFF757575)
val HistorySpentGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    totalSessions: Int = 0,
    totalSpent: Double = 0.0,
    hasSubscriptions: Boolean = false,
    onBrowsePackagesClicked: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Subscriptions", color = Color.Black) },
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
                selectedRoute = "history"
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(HistoryBackground)
                .padding(16.dp)
        ) {
            Text(
                text = "View your Wi-Fi subscription history",
                fontSize = 14.sp,
                color = HistoryTextGray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Total Sessions & Total Spent Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Total Sessions Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .padding(end = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = HistoryStatsBlue)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "$totalSessions",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = HistoryPrimaryBlue
                        )
                        Text(
                            text = "Total Sessions",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Total Spent Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .padding(start = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = HistoryStatsGreen)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "KSh ${totalSpent.toInt()}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = HistorySpentGreen
                        )
                        Text(
                            text = "Total Spent",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Subscription History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Subscription History Content
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                if (hasSubscriptions) {
                    // TODO: Implement LazyColumn to display actual subscription items here
                    // For now, it's just the "List of subscriptions will go here."
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("List of subscriptions will go here.", color = HistoryTextGray)
                    }
                    // Placing the Browse Packages button below the subscriptions list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = onBrowsePackagesClicked,
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(HistoryPrimaryBlue, HistoryPrimaryPurple)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Browse Packages",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 48.dp, horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dollar_sign),
                            contentDescription = "No Subscriptions Icon",
                            tint = Color.LightGray,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Subscriptions Yet",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You haven't purchased any Wi-Fi packages yet",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onBrowsePackagesClicked,
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(HistoryPrimaryBlue, HistoryPrimaryPurple)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Browse Packages",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "No Subscriptions")
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen(
        totalSessions = 0,
        totalSpent = 0.0,
        hasSubscriptions = false,
        onBrowsePackagesClicked = { /* preview */ },
        onNavigateToHome = { /* preview */ },
        onNavigateToPackages = { /* preview */ },
        onNavigateToHistory = { /* preview */ },
        onNavigateToProfile = { /* preview */ }
    )
}

@Preview(showBackground = true, name = "With Subscriptions")
@Composable
fun PreviewHistoryScreenWithData() {
    HistoryScreen(
        totalSessions = 5,
        totalSpent = 1250.0,
        hasSubscriptions = true,
        onBrowsePackagesClicked = { /* preview */ },
        onNavigateToHome = { /* preview */ },
        onNavigateToPackages = { /* preview */ },
        onNavigateToHistory = { /* preview */ },
        onNavigateToProfile = { /* preview */ }
    )
}
