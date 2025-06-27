package com.example.njwi_ficonnect.presentation.screens

import android.util.Log
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.navigation.compose.rememberNavController
import com.example.njwi_ficonnect.firebase.UserProfileRepository
import com.example.njwi_ficonnect.presentation.components.AuthViewModel
import com.example.njwi_ficonnect.presentation.navigation.BottomNavigationBar
import com.example.njwi_ficonnect.presentation.navigation.Routes
import com.example.njwi_ficonnect.presentation.viewmodel.ProfileViewModel

val ProfileBlue = Color(0xFF05E819)
val ProfilePurple = Color(0xFF96D714)
val ProfileBackground = Color(0xFFF0F2F5)
val ProfileSignOutRed = Color(0xFFE53935)
val ProfileCardGreen = Color(0xFF69D56F)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    authViewModel:AuthViewModel,
    onEditProfileClicked: () -> Unit,
    onSecurityClicked: () -> Unit,
    onNotificationsSettingsClicked: () -> Unit,
    onHelpSupportClicked: () -> Unit,
    onSignOutClicked: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    selectedRoute: String
) {
    // Optional: Notification toggles persisted to ViewModel or DataStore in a real app
    var isLowBalanceNotifEnabled by rememberSaveable { mutableStateOf(true) }
    var isSessionExpiryNotifEnabled by rememberSaveable { mutableStateOf(true) }
    var isPromotionsNotifEnabled by rememberSaveable { mutableStateOf(false) }
    var isSystemUpdatesNotifEnabled by rememberSaveable { mutableStateOf(false) }

    val userProfile by remember { derivedStateOf { profileViewModel.userProfile } }
    var refreshing by remember { mutableStateOf(false) }
    val navController = rememberNavController() // ✅ Or pass it in as a parameter


    // Pull-to-refresh simulation for profile reload
    fun refreshProfile() {
        refreshing = true
        profileViewModel.loadUserProfile()
        refreshing = false
    }

    Scaffold(
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
                    text = userProfile.name.ifBlank { "Your Name" },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Member since 2025",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Profile Information Card (GREEN)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ProfileCardGreen)
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
                                color = Color.White
                            )
                            IconButton(onClick = onEditProfileClicked) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Color.White)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        ProfileInfoRow(
                            icon = Icons.Default.Person,
                            label = "Full Name",
                            value = userProfile.name,
                            contentColor = Color.White
                        )
                        ProfileInfoRow(
                            icon = Icons.Default.Phone,
                            label = "Phone Number",
                            value = userProfile.phone,
                            contentColor = Color.White
                        )
                        ProfileInfoRow(
                            icon = Icons.Default.Email,
                            label = "Email Address",
                            value = userProfile.email,
                            contentColor = Color.White
                        )
                        if (profileViewModel.isUpdating) {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                color = Color.White
                            )
                        }
                        if (profileViewModel.errorMessage != null) {
                            Text(
                                text = profileViewModel.errorMessage!!,
                                color = Color.Red,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            item {
                // Notification Preferences Card (GREEN)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = ProfileCardGreen)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Notification Preferences",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        NotificationToggleRow(
                            label = "Low Balance",
                            description = "Get notified when your balance is low",
                            isChecked = isLowBalanceNotifEnabled,
                            onCheckedChange = { isLowBalanceNotifEnabled = it },
                            labelColor = Color.White
                        )
                        NotificationToggleRow(
                            label = "Session Expiry",
                            description = "Alerts before your session expires",
                            isChecked = isSessionExpiryNotifEnabled,
                            onCheckedChange = { isSessionExpiryNotifEnabled = it },
                            labelColor = Color.White
                        )
                        NotificationToggleRow(
                            label = "Promotions",
                            description = "Receive promotional offers and discounts",
                            isChecked = isPromotionsNotifEnabled,
                            onCheckedChange = { isPromotionsNotifEnabled = it },
                            labelColor = Color.White
                        )
                        NotificationToggleRow(
                            label = "System Updates",
                            description = "Important system updates and maintenance",
                            isChecked = isSystemUpdatesNotifEnabled,
                            onCheckedChange = { isSystemUpdatesNotifEnabled = it },
                            labelColor = Color.White
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
                Button(
                    onClick = {
                        UserProfileRepository.deleteUserProfile { success, error ->
                            if (success) {
                                // Navigate to login screen or logout
                                navController.navigate(Routes.AUTH) {
                                    popUpTo(Routes.HOME) { inclusive = true }
                                }

                            } else {
                                Log.e("Delete", "Error: $error")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete My Profile")
                }
                Spacer(modifier = Modifier.height(24.dp))
//                Button(onClick = { authViewModel.checkEmailVerification() }) {
//                    Text("I've Verified My Email")
//                }
//


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
fun ProfileInfoRow(
    icon: ImageVector,
    label: String,
    value: String,
    contentColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = contentColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = contentColor.copy(alpha = 0.7f))
            Text(text = value.ifBlank { "—" }, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = contentColor)
        }
    }
}

@Composable
fun NotificationToggleRow(
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
            Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = labelColor)
            Text(text = description, fontSize = 12.sp, color = labelColor.copy(alpha = 0.7f))
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
    val dummyViewModel = object : ProfileViewModel() {}
    ProfileScreen(
        profileViewModel = dummyViewModel,
        onEditProfileClicked = { /* preview */ },
        onSecurityClicked = { /* preview */ },
        onNotificationsSettingsClicked = { /* preview */ },
        onHelpSupportClicked = { /* preview */ },
        onSignOutClicked = { /* preview */ },
        onNavigateToHome = { /* preview */ },
        onNavigateToPackages = { /* preview */ },
        onNavigateToHistory = { /* preview */ },
        onNavigateToProfile = { /* preview */ },
        selectedRoute = Routes.PROFILE,
        authViewModel = TODO()
    )
}