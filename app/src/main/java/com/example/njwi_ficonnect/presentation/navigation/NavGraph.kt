package com.example.njwi_ficonnect.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.njwi_ficonnect.presentation.components.ForgotPasswordScreen
import com.example.njwi_ficonnect.presentation.components.WifiLoginScreen
import com.example.njwi_ficonnect.presentation.screens.HomeScreen
import com.example.njwi_ficonnect.presentation.screens.PackagesScreen
import com.example.njwi_ficonnect.presentation.screens.ConfirmPurchaseScreen
import com.example.njwi_ficonnect.presentation.screens.HistoryScreen
import com.example.njwi_ficonnect.presentation.screens.ProfileScreen

object Routes {
    const val AUTH = "auth"
    const val FORGOT_PASSWORD = "forgotpassword"
    const val HOME = "home"
    const val PACKAGES = "packages"
    const val CONFIRM_PURCHASE = "confirm_purchase"
    const val HISTORY = "history"
    const val PROFILE = "profile"
}

@Composable
fun WifiNavGraph(navController: NavHostController) {
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
//            HomeScreen(
//                navController = navController,
//                onPackagesClicked = { navController.navigate(Routes.PACKAGES) },
//                onHistoryClicked = { navController.navigate(Routes.HISTORY) },
//                onProfileClicked = { navController.navigate(Routes.PROFILE) }
//            )
        }
        composable(Routes.PACKAGES) {
//            PackagesScreen(
//                navController = navController,
//                onPurchaseClicked = { navController.navigate(Routes.CONFIRM_PURCHASE) }
//            )
        }
        composable(Routes.CONFIRM_PURCHASE) {
//            ConfirmPurchaseScreen(
////                navController = navController,
//                onPurchaseConfirmed = { navController.popBackStack(Routes.PACKAGES, inclusive = false) },
//                onCancel = { navController.popBackStack(Routes.PACKAGES, inclusive = false) }
//            )
        }
        composable(Routes.HISTORY) {
//            HistoryScreen(
//                navController = navController,
//                onBackClicked = { navController.popBackStack(Routes.HOME, inclusive = false) }
//            )
        }
        composable(Routes.PROFILE) {
//            ProfileScreen(
//                navController = navController,
//                onLogoutClicked = {
//                    navController.navigate(Routes.AUTH) {
//                        popUpTo(Routes.HOME) { inclusive = true }
//                    }
//                }
//            )
        }
    }
}