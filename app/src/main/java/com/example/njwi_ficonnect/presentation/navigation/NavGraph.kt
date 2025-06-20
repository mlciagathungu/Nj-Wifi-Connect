package com.example.njwi_ficonnect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.njwi_ficonnect.presentation.components.ForgotPasswordScreen
import com.example.njwi_ficonnect.presentation.components.WifiLoginScreen
import com.example.njwi_ficonnect.presentation.screens.HomeScreen
import com.example.njwi_ficonnect.presentation.screens.PackagesScreen
import com.example.njwi_ficonnect.presentation.screens.ConfirmPurchaseScreen
import com.example.njwi_ficonnect.presentation.screens.HistoryScreen
import com.example.njwi_ficonnect.presentation.screens.ProfileScreen
import com.example.njwi_ficonnect.presentation.screens.EditProfileScreen
import com.example.njwi_ficonnect.presentation.viewmodel.HistoryViewModel
import com.example.njwi_ficonnect.presentation.viewmodel.ProfileViewModel

object Routes {
    const val AUTH = "auth"
    const val FORGOT_PASSWORD = "forgotpassword"
    const val HOME = "home"
    const val PACKAGES = "packages"
    const val CONFIRM_PURCHASE = "confirm_purchase"
    const val HISTORY = "history"
    const val PROFILE = "profile"
    const val EDIT_PROFILE = "edit_profile"
}

@Composable
fun WifiNavGraph(navController: NavHostController) {
    val historyViewModel: HistoryViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.AUTH) {
        composable(Routes.AUTH) {
            WifiLoginScreen(
                navController = navController,
                onForgotPasswordClicked = { navController.navigate(Routes.FORGOT_PASSWORD) }
            )
        }
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                navController = navController,
                onResetPasswordClicked = { navController.popBackStack(Routes.AUTH, inclusive = false) },
                onBackToLoginClicked = { navController.popBackStack(Routes.AUTH, inclusive = false) }
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                userName = "User",
                onBrowsePackagesClicked = { navController.navigate(Routes.PACKAGES) },
                onMySubscriptionsClicked = { navController.navigate(Routes.HISTORY) },
                onAccountSettingsClicked = { navController.navigate(Routes.PROFILE) },
                onNavigateToHome = { if (navController.currentDestination?.route != Routes.HOME) navController.navigate(Routes.HOME) },
                onNavigateToPackages = { navController.navigate(Routes.PACKAGES) },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) },
                selectedRoute = Routes.HOME
            )
        }
        composable(Routes.PACKAGES) {
            PackagesScreen(
                onNavigateToConfirmPurchase = { name, description, duration, access, price ->
                    navController.navigate(
                        "${Routes.CONFIRM_PURCHASE}/$name/$description/$duration/$access/$price"
                    )
                },
                onNavigateToHome = { navController.navigate(Routes.HOME) },
                onNavigateToPackages = { /* already here */ },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(
            route = "${Routes.CONFIRM_PURCHASE}/{packageName}/{packageDescription}/{packageDuration}/{packageAccess}/{packagePrice}",
            arguments = listOf(
                navArgument("packageName") { type = NavType.StringType },
                navArgument("packageDescription") { type = NavType.StringType },
                navArgument("packageDuration") { type = NavType.StringType },
                navArgument("packageAccess") { type = NavType.StringType },
                navArgument("packagePrice") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val packageName = backStackEntry.arguments?.getString("packageName") ?: ""
            val packageDescription = backStackEntry.arguments?.getString("packageDescription") ?: ""
            val packageDuration = backStackEntry.arguments?.getString("packageDuration") ?: ""
            val packageAccess = backStackEntry.arguments?.getString("packageAccess") ?: ""
            val packagePrice = backStackEntry.arguments?.getFloat("packagePrice")?.toDouble() ?: 0.0

            ConfirmPurchaseScreen(
                packageName = packageName,
                packageDescription = packageDescription,
                packageDuration = packageDuration,
                packageAccess = packageAccess,
                packagePrice = packagePrice,
                onBackClicked = { navController.popBackStack() },
                onPurchaseConfirmed = {
                    navController.popBackStack(Routes.PACKAGES, inclusive = false)
                },
                onCancel = { navController.popBackStack(Routes.PACKAGES, inclusive = false) },
                historyViewModel = historyViewModel
            )
        }
        composable(Routes.HISTORY) {
            HistoryScreen(
                onNavigateToHome = { navController.navigate(Routes.HOME) },
                onNavigateToPackages = { navController.navigate(Routes.PACKAGES) },
                onNavigateToHistory = { /* already here */ },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) },
                selectedRoute = Routes.HISTORY,
                onBrowsePackagesClicked = { navController.navigate(Routes.PACKAGES) },
                historyViewModel = historyViewModel
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onEditProfileClicked = { navController.navigate(Routes.EDIT_PROFILE) },
                onSecurityClicked = { },
                onNotificationsSettingsClicked = { },
                onHelpSupportClicked = { },
                onNavigateToHome = { navController.navigate(Routes.HOME) },
                onNavigateToPackages = { navController.navigate(Routes.PACKAGES) },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                onNavigateToProfile = { /* already here */ },
                onSignOutClicked = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                selectedRoute = Routes.PROFILE
            )
        }
        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                profileViewModel = profileViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

// Add this Preview below your WifiNavGraph function or in the same file:
@Preview(showBackground = true)
@Composable
fun PreviewWholeApp() {
    val navController = rememberNavController()
    WifiNavGraph(navController = navController)
}