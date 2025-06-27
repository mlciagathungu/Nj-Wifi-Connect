package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    // In a real app, connect these to ViewModel/DataStore/Firestore
    initialLowBalance: Boolean = true,
    initialSessionExpiry: Boolean = true,
    initialPromotions: Boolean = false,
    initialSystemUpdates: Boolean = false,
    onSettingChanged: ((String, Boolean) -> Unit)? = null
) {
    var isLowBalanceNotifEnabled by rememberSaveable { mutableStateOf(initialLowBalance) }
    var isSessionExpiryNotifEnabled by rememberSaveable { mutableStateOf(initialSessionExpiry) }
    var isPromotionsNotifEnabled by rememberSaveable { mutableStateOf(initialPromotions) }
    var isSystemUpdatesNotifEnabled by rememberSaveable { mutableStateOf(initialSystemUpdates) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification Settings") },
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
            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top)
        ) {
            Text(
                "Manage your notification preferences below.",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            NotificationToggleRo(
                label = "Low Balance",
                description = "Get notified when your balance is low",
                isChecked = isLowBalanceNotifEnabled,
                onCheckedChange = {
                    isLowBalanceNotifEnabled = it
                    onSettingChanged?.invoke("low_balance", it)
                }
            )
            NotificationToggleRo(
                label = "Session Expiry",
                description = "Alerts before your session expires",
                isChecked = isSessionExpiryNotifEnabled,
                onCheckedChange = {
                    isSessionExpiryNotifEnabled = it
                    onSettingChanged?.invoke("session_expiry", it)
                }
            )
            NotificationToggleRo(
                label = "Promotions",
                description = "Receive promotional offers and discounts",
                isChecked = isPromotionsNotifEnabled,
                onCheckedChange = {
                    isPromotionsNotifEnabled = it
                    onSettingChanged?.invoke("promotions", it)
                }
            )
            NotificationToggleRo(
                label = "System Updates",
                description = "Important updates and maintenance alerts",
                isChecked = isSystemUpdatesNotifEnabled,
                onCheckedChange = {
                    isSystemUpdatesNotifEnabled = it
                    onSettingChanged?.invoke("system_updates", it)
                }
            )
        }
    }
}

@Composable
fun NotificationToggleRo(
    label: String,
    description: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    labelColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 16.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium, color = labelColor)
            Text(text = description, fontSize = 12.sp, color = labelColor.copy(alpha = 0.7f))
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF05E819),
                checkedTrackColor = Color(0xFF05E819).copy(alpha = 0.5f)
            )
        )
    }
}