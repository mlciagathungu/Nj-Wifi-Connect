package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.njwi_ficonnect.R // Make sure this points to your app's R file
import com.example.njwi_ficonnect.data.remote.api.RetrofitInstance
import com.example.njwi_ficonnect.data.remote.model.StkPushRequest
import kotlinx.coroutines.launch

// Color definitions
val BrandBlue = Color(0xFF4A90E2)
val AccentPurple = Color(0xFF9B59B6)
val MpesaGreen = Color(0xFF4CAF50)
val SecureBadgeYellow = Color(0xFFFFF9C4)
val SecureBadgeText = Color(0xFFC58400)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPurchaseScreen(
    packageName: String,
    packageDescription: String,
    packageDuration: String,
    packageAccess: String,
    packagePrice: Double,
    onBackClicked: () -> Unit
) {
    var mpesaPhoneNumber by remember { mutableStateOf("0712345678") }
    var apiMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Purchase", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.Info, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB8ECA9)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF0F2F5))
                .padding(16.dp)
        ) {
            // Package Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = packageName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = packageDescription,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_duration),
                                contentDescription = "Duration",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = packageDuration, fontSize = 14.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.width(16.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_data_allowance),
                                contentDescription = "Data Access",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = packageAccess, fontSize = 14.sp, color = Color.Gray)
                        }
                        Text(
                            text = "KSh ${packagePrice.toInt()}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }

            // Payment Method Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Payment Method",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MpesaGreen,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_mpesa),
                            contentDescription = "M-Pesa Logo",
                            tint = MpesaGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "M-Pesa", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                            Text(text = "Pay with your mobile money", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // M-Pesa Phone Number Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "M-Pesa Phone Number",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = mpesaPhoneNumber,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() } && newValue.length <= 10) {
                                mpesaPhoneNumber = newValue
                            }
                        },
                        label = { Text("0712345678") },
                        leadingIcon = { Icon(Icons.Default.Info, contentDescription = "Phone Icon") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You will receive an M-Pesa prompt on this number",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Secure Payment Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SecureBadgeYellow)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Secure Payment",
                    tint = SecureBadgeText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Secure Payment",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SecureBadgeText
                    )
                    Text(
                        text = "Your payment is processed securely through Safaricom M-Pesa",
                        fontSize = 12.sp,
                        color = SecureBadgeText
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Purchase Button
            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        apiMessage = ""
                        try {
                            val response = RetrofitInstance.api.stkPush(
                                StkPushRequest(
                                    phone_number = "254${mpesaPhoneNumber.trimStart('0')}",
                                    amount = packagePrice,
                                    account_reference = packageName
                                )
                            )
                            apiMessage = response.CustomerMessage
                                ?: response.ResponseDescription
                                        ?: response.errorMessage
                                        ?: if (response.success) "STK Push sent. Check your phone!"
                                else "Unknown error. Try again."
                        } catch (e: Exception) {
                            apiMessage = "Error: ${e.localizedMessage}"
                        } finally {
                            isLoading = false
                        }
                    }
                },
                enabled = !isLoading && mpesaPhoneNumber.length == 10 && mpesaPhoneNumber.startsWith("07"),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(BrandBlue, AccentPurple)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Confirm Purchase - KSh ${packagePrice.toInt()}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Show API message
            if (apiMessage.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = apiMessage,
                    color = if (apiMessage.startsWith("Error") || apiMessage.contains("fail", true)) Color.Red else Color(0xFF008000),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmPurchaseScreenDailyEssential() {
    ConfirmPurchaseScreen(
        packageName = "Daily Essential",
        packageDescription = "Great for work and social media",
        packageDuration = "1d",
        packageAccess = "2.0GB",
        packagePrice = 150.0,
        onBackClicked = { }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmPurchaseScreenBusinessPackage() {
    ConfirmPurchaseScreen(
        packageName = "Business Package",
        packageDescription = "Premium unlimited access for businesses",
        packageDuration = "14d",
        packageAccess = "Unlimited",
        packagePrice = 1200.0,
        onBackClicked = { }
    )
}