package com.example.campuscompanion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campuscompanion.feature.auth.signin.SignInScreen

@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController();
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(route = Screen.LoginScreen.route) {
            SignInScreen(navController = navController)
        }
    }
}