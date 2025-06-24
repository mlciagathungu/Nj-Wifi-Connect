package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityScreen(
    onBack: () -> Unit,
    // Accepts a callback to send password reset email, or connect to ViewModel in real app
    onSendPasswordReset: ((String) -> Unit)? = null
) {
    var email by remember { mutableStateOf("") }
    var showResetDialog by remember { mutableStateOf(false) }
    var resetMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Security") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top)
        ) {
            Text(
                "Account Security Options",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                "For your safety, you can reset your password below. We recommend using a strong, unique password for your account.",
                color = Color.Gray
            )

            Button(
                onClick = { showResetDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Reset Password")
            }

            if (resetMessage != null) {
                Text(
                    resetMessage!!,
                    color = if (resetMessage!!.contains("sent", true)) Color(0xFF388E3C) else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            // Add more options (2FA, device management, etc.) here in the future.
        }

        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Reset Password") },
                text = {
                    Column {
                        Text("Enter your email address to receive a password reset link.")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            singleLine = true,
                            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Done
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            isLoading = true
                            resetMessage = null
                            // Simulate async password reset
                            onSendPasswordReset?.invoke(email)
                            // In a real app, handle result via ViewModel/callback
                            resetMessage = "If this email is registered, a password reset link has been sent."
                            isLoading = false
                            showResetDialog = false
                        },
                        enabled = !isLoading && email.isNotBlank(),
                    ) {
                        if (isLoading) CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        else Text("Send Reset Email")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun KeyboardOptions(keyboardType: KeyboardType, imeAction: ImeAction) {
    TODO("Not yet implemented")
}

@Composable
fun KeyboardOptions(keyboardType: KeyboardType, imeAction: ImeAction) {
    TODO("Not yet implemented")
}