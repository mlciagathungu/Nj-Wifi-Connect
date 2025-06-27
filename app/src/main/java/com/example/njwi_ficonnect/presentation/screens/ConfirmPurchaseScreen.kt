package com.example.njwi_ficonnect.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.njwi_ficonnect.R
import com.example.njwi_ficonnect.firebase.PaymentHistoryRepository
import com.example.njwi_ficonnect.network.RetrofitClient
import com.example.njwi_ficonnect.network.model.MpesaRequest
import com.example.njwi_ficonnect.network.model.MpesaResponse
import com.example.njwi_ficonnect.presentation.viewmodel.HistoryViewModel
import com.example.njwi_ficonnect.presentation.viewmodel.saveToFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import com.example.njwi_ficonnect.firebase.PaymentRecord



val BrandBlue = Color(0xFF4A90E2)
val AccentPurple = Color(0xFF9B59B6)
val MpesaGreen = Color(0xFF4CAF50)
val SecureBadgeYellow = Color(0xFFFFF9C4)
val SecureBadgeText = Color(0xFFC58400)

// Declare state at the top of your Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmPurchaseScreen(
    navController: NavHostController, // 👈 Add this
    packageName: String,
    packageDescription: String,
    packageDuration: String,
    packageAccess: String,
    packagePrice: Double,
    HistoryViewModel: HistoryViewModel, // ✅ Add this line
    onBackClicked: () -> Unit,
    onCancel: () -> Unit,
) {
    var mpesaPhoneNumber by remember { mutableStateOf("0712345678") }
    var apiMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showConfirmationButtons by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirm Purchase", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFB8ECA9)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F2F5))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
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

                // Confirm Purchase Button (Step 1)
                if (!showConfirmationButtons) {
                    Button(
                        onClick = {
                            showConfirmationButtons = true
                        },
                        enabled = !isLoading && mpesaPhoneNumber.length == 10 && mpesaPhoneNumber.startsWith("07"),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Confirm Purchase - KSh ${packagePrice.toInt()}",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (showConfirmationButtons) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 16.dp),
                            strokeWidth = 2.dp
                        )
                    }
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                isLoading = true
                                apiMessage = ""
                                try {
                                    val formattedPhone = if (mpesaPhoneNumber.startsWith("07"))
                                        "254${mpesaPhoneNumber.drop(1)}"
                                    else mpesaPhoneNumber
                                    val request = MpesaRequest(
                                        phone_number = formattedPhone,
                                        amount = packagePrice,
                                        account_reference = "NjConnect-${System.currentTimeMillis()}",
                                        description = packageDescription
                                    )
                                    val response = RetrofitClient.api.initiateStkPush(request)
                                    if (response.isSuccessful) {
                                        val data: MpesaResponse? = response.body()
                                        val checkoutId = data?.CheckoutRequestID ?: "" // ✅ Define it here
                                        apiMessage = data?.CustomerMessage ?: "STK push initiated. Awaiting confirmation."

                                        // Wait 6 seconds for Safaricom to send callback
                                        delay(6000)
                                        val statusResponse = RetrofitClient.api.getTransactionStatus(checkoutId)
                                        if (statusResponse.isSuccessful) {
                                            val tx = statusResponse.body()
                                            if (tx?.status == "completed") {
                                                val paymentRecord = PaymentRecord(
                                                    id = "", // Leave empty, Firestore will assign one
                                                    phone = tx.phone,
                                                    receipt = tx.receipt?.toString() ?: "Unknown",
                                                    amount = tx.amount,
                                                    reference = "NjConnect-${System.currentTimeMillis()}", // Or tx.reference if available
                                                    description = tx.description,
                                                    status = tx.status.uppercase(),
                                                    userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
                                                )

                                                PaymentHistoryRepository.addPaymentRecord(paymentRecord) { success, error ->
                                                    if (!success) {
                                                        Log.e("ConfirmPurchase", "Failed to save payment: $error")
                                                    }
                                                }
                                                apiMessage = "Payment successful: ${tx.receipt}"
                                                saveToFirestore(tx) // Optional
                                                // ✅ Add this:
                                                HistoryViewModel.RecordSessionPayment(
                                                    sessionId = tx.receipt ?: "TX-${System.currentTimeMillis()}",
                                                    packageName = packageName,
                                                    amount = packagePrice,
                                                    validityDays = 1, // or derive from package info
                                                    isActive = true
                                                )
                                                navController.navigate("payment_success")
                                            } else {
                                                apiMessage = "Payment not completed yet. Try again shortly."
                                            }
                                        } else {
                                            apiMessage = "Error checking status: ${statusResponse.code()}"
                                        }
                                        navController.navigate("payment_success") // 👈 Navigate to success screen
                                    } else {
                                        apiMessage = "Error: ${response.errorBody()?.string()}"
                                    }
                                } catch (e: IOException) {
                                    apiMessage = "Network error: ${e.localizedMessage}"
                                } catch (e: HttpException) {
                                    apiMessage = "HTTP error: ${e.localizedMessage}"
                                } catch (e: Exception) {
                                    apiMessage = "Unexpected error: ${e.localizedMessage}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        enabled = !isLoading && mpesaPhoneNumber.length == 10 && mpesaPhoneNumber.startsWith("07"),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(top = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandBlue
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "Proceed & Pay",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    OutlinedButton(
                        onClick = {
                            showConfirmationButtons = false
                            apiMessage = ""
                            onCancel()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", color = AccentPurple, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

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
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

