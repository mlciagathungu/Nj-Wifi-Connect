package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.njwi_ficonnect.presentation.navigation.BottomNavigationBar
import com.example.njwi_ficonnect.presentation.viewmodel.HistoryViewModel
import com.example.njwi_ficonnect.presentation.viewmodel.PackageHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyViewModel: HistoryViewModel,
    packageHistoryViewModel: PackageHistoryViewModel, // ✅ add this
    onNavigateToHome: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    selectedRoute: String,
    onBrowsePackagesClicked: () -> Unit
) {
    val sessionPayments by historyViewModel.sessionPayments.collectAsStateWithLifecycle()
    val hasActiveSession = historyViewModel.hasActiveSubscription
    val totalSpent = historyViewModel.totalSpent
    val totalSessions = historyViewModel.totalSessions
    val activeCount = packageHistoryViewModel.activeCount
    val expiredCount = packageHistoryViewModel.expiredCount
    val isLoading = packageHistoryViewModel.isLoading
    val errorMessage = packageHistoryViewModel.errorMessage


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My History") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Package History", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextButton(onClick = { packageHistoryViewModel.refresh() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    Text("Refresh")
                }
            }

            if (sessionPayments.isEmpty()) {
                EmptyHistoryState(onBrowsePackagesClicked)
            } else {
                SummaryCard(
                    totalSessions = totalSessions,
                    totalSpent = totalSpent,
                    hasActiveSession = hasActiveSession
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Active Packages: $activeCount", color = Color(0xFF4CAF50))
                    Text("Expired Packages: $expiredCount", color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                errorMessage?.let {
                    Text(text = it, color = Color.Red, fontSize = 14.sp)
                }

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(sessionPayments.reversed()) { payment ->
                        HistoryItemCard(
                            packageName = payment.packageName,
                            amount = payment.amount,
                            timestamp = payment.timestamp,
                            isActive = payment.isActive
                        )
                    }
                }
            }
        }
    }



}

@Composable
fun SummaryCard(totalSessions: Int, totalSpent: Double, hasActiveSession: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFCAEAA9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Total Sessions: $totalSessions", color = Color.Black)
            Text("Total Spent: KSh ${totalSpent.toInt()}", color = Color.Black)
            Text(
                "Active Session: ${if (hasActiveSession) "Yes" else "No"}",
                color = if (hasActiveSession) Color(0xFF4CAF50) else Color.Gray
            )
        }
    }
}

@Composable
fun HistoryItemCard(packageName: String, amount: Double, timestamp: Long, isActive: Boolean) {
    val dateFormatted = remember(timestamp) {
        SimpleDateFormat("d MMM yyyy, h:mm a", Locale.getDefault()).format(Date(timestamp))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(packageName, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(dateFormatted, fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("KSh ${amount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    if (isActive) "Active" else "Expired",
                    color = if (isActive) Color(0xFF4CAF50) else Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun EmptyHistoryState(onBrowsePackagesClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Refresh,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("No session history yet.", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onBrowsePackagesClicked) {
            Text("Browse Packages")
        }
    }
}
