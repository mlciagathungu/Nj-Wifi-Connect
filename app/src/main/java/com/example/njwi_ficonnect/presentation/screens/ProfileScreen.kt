package com.example.njwi_ficonnect.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.njwi_ficonnect.presentation.navigation.BottomNavigationBar

// Renamed color vals for clarity and consistency
val ProfileBlue = Color(0xFF4A90E2)
val ProfilePurple = Color(0xFF9B59B6)
val ProfileBackground = Color(0xFFF0F2F5) // Background color
val ProfileSignOutRed = Color(0xFFE53935) // Red for Sign Out

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String = "Micia Gathungu",
    memberSince: String = "2025",
    phoneNumber: String = "0793023967",
    emailAddress: String = "njgathungu23240@gmail.com",
    onEditProfileClicked: () -> Unit,
    onSecurityClicked: () -> Unit,
    onNotificationsSettingsClicked: () -> Unit,
    onHelpSupportClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    // State for notification toggles (renamed for clarity)
    var isLowBalanceNotifEnabled by remember { mutableStateOf(true) }
    var isSessionExpiryNotifEnabled by remember { mutableStateOf(true) }
    var isPromotionsNotifEnabled by remember { mutableStateOf(false) }
    var isSystemUpdatesNotifEnabled by remember { mutableStateOf(false) }

    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { /* Empty, header is in LazyColumn */ },
//                backgroundColor = Color.Transparent,
//                elevation = 0.dp,
//                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//            )
//        },
        bottomBar = {
            BottomNavigationBar(
                onNavigateToHome = onNavigateToHome,
                onNavigateToPackages = onNavigateToPackages,
                onNavigateToHistory = onNavigateToHistory,
                onNavigateToProfile = onNavigateToProfile,
                selectedRoute = "profile"
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(ProfileBackground)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                // Profile Header
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(ProfileBlue, ProfilePurple)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.size(64.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = userName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Member since $memberSince",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Profile Information Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Profile Information",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            IconButton(onClick = onEditProfileClicked) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = ProfileBlue)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileInfoRow(
                            icon = Icons.Default.Person,
                            label = "Full Name",
                            value = userName
                        )
                        ProfileInfoRow(
                            icon = Icons.Default.Phone,
                            label = "Phone Number",
                            value = phoneNumber
                        )
                        ProfileInfoRow(
                            icon = Icons.Default.Email,
                            label = "Email Address",
                            value = emailAddress
                        )
                    }
                }
            }

            item {
                // Notification Preferences Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Notification Preferences",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NotificationToggleRow(
                            label = "Low Balance",
                            description = "Get notified when your balance is low",
                            isChecked = isLowBalanceNotifEnabled,
                            onCheckedChange = { isLowBalanceNotifEnabled = it }
                        )
                        NotificationToggleRow(
                            label = "Session Expiry",
                            description = "Alerts before your session expires",
                            isChecked = isSessionExpiryNotifEnabled,
                            onCheckedChange = { isSessionExpiryNotifEnabled = it }
                        )
                        NotificationToggleRow(
                            label = "Promotions",
                            description = "Receive promotional offers and discounts",
                            isChecked = isPromotionsNotifEnabled,
                            onCheckedChange = { isPromotionsNotifEnabled = it }
                        )
                        NotificationToggleRow(
                            label = "System Updates",
                            description = "Important system updates and maintenance",
                            isChecked = isSystemUpdatesNotifEnabled,
                            onCheckedChange = { isSystemUpdatesNotifEnabled = it }
                        )
                    }
                }
            }

            item {
                OptionItem(
                    icon = Icons.Default.Lock,
                    title = "Security",
                    description = "Change password & security settings",
                    onClick = onSecurityClicked
                )
                OptionItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    description = "Manage your notification preferences",
                    onClick = onNotificationsSettingsClicked
                )
                OptionItem(
                    icon = Icons.Default.Lock,
                    title = "Help & Support",
                    description = "Get help and contact support",
                    onClick = onHelpSupportClicked
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                // Sign Out Button
                Button(
                    onClick = onSignOutClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ProfileSignOutRed, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.ArrowForward, contentDescription = "Sign Out", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Sign Out",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "NJ Connect Wi-Fi v1.0.0",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "© 2025 NJ Connect. All rights reserved.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = ProfileBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = Color.Gray)
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
        }
    }
}

@Composable
fun NotificationToggleRow(label: String, description: String, isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
            Text(text = description, fontSize = 12.sp, color = Color.Gray)
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = ProfileBlue,
                checkedTrackColor = ProfileBlue.copy(alpha = 0.5f)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionItem(icon: ImageVector, title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = title, tint = ProfileBlue, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                Text(description, fontSize = 12.sp, color = Color.Gray)
            }
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Go", tint = Color.LightGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen(
        onEditProfileClicked = { /* preview */ },
        onSecurityClicked = { /* preview */ },
        onNotificationsSettingsClicked = { /* preview */ },
        onHelpSupportClicked = { /* preview */ },
        onSignOutClicked = { /* preview */ },
        onNavigateToHome = { /* preview */ },
        onNavigateToPackages = { /* preview */ },
        onNavigateToHistory = { /* preview */ },
        onNavigateToProfile = { /* preview */ }
    )
}
