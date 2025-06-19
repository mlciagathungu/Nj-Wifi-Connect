package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.njwi_ficonnect.presentation.navigation.Routes
import com.example.njwi_ficonnect.presentation.viewmodel.HistoryViewModel
import com.example.njwi_ficonnect.presentation.viewmodel.SessionPayment
import androidx.lifecycle.viewmodel.compose.viewModel

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
    onBrowsePackagesClicked: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    selectedRoute: String,
    historyViewModel: HistoryViewModel = viewModel()
) {
    val sessionPayments by historyViewModel.sessionPayments.collectAsState()
    val totalSessions = historyViewModel.totalSessions
    val totalSpent = historyViewModel.totalSpent
    val hasSubscriptions = historyViewModel.hasActiveSubscription

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
                if (sessionPayments.isNotEmpty()) {
                    // Show the sessionPayments in a LazyColumn
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 120.dp, max = 240.dp)
                    ) {
                        items(sessionPayments.reversed()) { payment ->
                            SubscriptionHistoryItem(payment)
                            Divider()
                        }
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

@Composable
fun SubscriptionHistoryItem(payment: SessionPayment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 16.dp)
    ) {
        Text(
            text = payment.packageName,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
        Text(
            text = "KSh ${payment.amount.toInt()}",
            fontWeight = FontWeight.Medium,
            color = HistorySpentGreen,
            fontSize = 14.sp
        )
        Text(
            text = "Active: ${if (payment.isActive) "Yes" else "No"}",
            fontSize = 12.sp,
            color = if (payment.isActive) Color(0xFF388E3C) else Color(0xFFD32F2F)
        )
        Text(
            text = "Date: ${java.text.SimpleDateFormat("d MMM yyyy, h:mm a").format(java.util.Date(payment.timestamp))}",
            fontSize = 12.sp,
            color = HistoryTextGray
        )
    }
}

@Preview(showBackground = true, name = "With Subscriptions")
@Composable
fun PreviewHistoryScreenWithData() {
    // Fake ViewModel for preview
    val fakePayments = listOf(
        SessionPayment(
            sessionId = "1",
            packageName = "Daily Essential",
            amount = 150.0,
            timestamp = System.currentTimeMillis(),
            isActive = true
        ),
        SessionPayment(
            sessionId = "2",
            packageName = "Business Package",
            amount = 1200.0,
            timestamp = System.currentTimeMillis() - 86400000L,
            isActive = false
        )
    )
    val fakeViewModel = object : HistoryViewModel() {
        init {
            _sessionPayments.value = fakePayments
        }
    }
    HistoryScreen(
        onBrowsePackagesClicked = { /* preview */ },
        onNavigateToHome = { /* preview */ },
        onNavigateToPackages = { /* preview */ },
        onNavigateToHistory = { /* preview */ },
        onNavigateToProfile = { /* preview */ },
        selectedRoute = Routes.HISTORY,
        historyViewModel = fakeViewModel
    )
}