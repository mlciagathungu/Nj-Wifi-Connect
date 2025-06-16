package com.example.njwi_ficonnect.presentation.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.njwi_ficonnect.R

data class BottomNavItem(
    val label: String,
    val iconRes: Int,
    val route: String
)

@Composable
fun BottomNavigationBar(
    onNavigateToHome: () -> Unit,
    onNavigateToPackages: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToProfile: () -> Unit,
    selectedRoute: String
) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.ic_home, "home"),
        BottomNavItem("Packages", R.drawable.ic_packages, "packages"),
        BottomNavItem("History", R.drawable.ic_history, "history"),
        BottomNavItem("Profile", R.drawable.ic_profile, "profile"),
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.iconRes), contentDescription = item.label) },
                label = { Text(item.label) },
                selected = item.route == selectedRoute,
                onClick = {
                    when (item.route) {
                        "home" -> onNavigateToHome()
                        "packages" -> onNavigateToPackages()
                        "history" -> onNavigateToHistory()
                        "profile" -> onNavigateToProfile()
                    }
                }
            )
        }
    }
}