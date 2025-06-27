package com.example.njwi_ficonnect.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.njwi_ficonnect.presentation.viewmodel.PaymentHistoryViewModel
import com.example.njwi_ficonnect.firebase.PaymentRecord
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentHistoryScreen(
    viewModel: PaymentHistoryViewModel = viewModel()
) {
    val payments = viewModel.payments
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.loadPayments()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment History", color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB8ECA9)
                )
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            if (!errorMessage.isNullOrBlank()) {
                Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
            }

            if (payments.isEmpty() && !isLoading) {
                Text("No payment history found.", color = Color.Gray)
            }
            Button(
                onClick = { viewModel.loadPayments() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Refresh Payment History")
            }
            Button(
                onClick = {
                    val end = System.currentTimeMillis()
                    val start = end - (7 * 24 * 60 * 60 * 1000L) // Last 7 days
                    viewModel.loadPaymentsByDateRange(start, end)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text("Show Last 7 Days")
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(payments) { payment ->
                    PaymentHistoryCard(payment)
                }
            }
        }
    }
}

@Composable
fun PaymentHistoryCard(payment: PaymentRecord) {
    val formattedDate = payment.getFormattedDate()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Amount: KSh ${payment.getFormattedAmount()}", fontWeight = FontWeight.Bold)
            Text(text = "Receipt: ${payment.receipt}", fontSize = 14.sp, color = Color.Gray)
            Text(text = "Phone: ${payment.phone}", fontSize = 14.sp, color = Color.Gray)
            Text(text = "${payment.description}", fontSize = 14.sp, color = Color.Gray)
            Text(text = "Date: $formattedDate", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
