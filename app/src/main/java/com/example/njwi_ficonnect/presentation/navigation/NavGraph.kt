package com.example.todo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.njwi_ficonnect.presentation.components.ForgotPasswordScreen
import com.example.njwi_ficonnect.presentation.components.WifiLoginScreen

// INSIDE THIS FILE WE WILL DEFINE NAVCONTROLLER : THIS IS USED TO NAVIGATE
// TO DIFFERENT COMPOSABLES / SCREENS
@Composable
fun WifiNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = "signUp") {
        composable("signUp"){
            WifiLoginScreen(
                navController,
                onSignInClicked = TODO(),
                onSignUpClicked = TODO(),
                onForgotPasswordClicked = TODO()
            )
        }

        composable("forgotpassword"){
            ForgotPasswordScreen(
                navController,
                onResetPasswordClicked = TODO(),
                onBackToLoginClicked = TODO()
            )
        }

        // here will define the addtoDo composable

    }

}




