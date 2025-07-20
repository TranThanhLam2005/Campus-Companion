package com.example.campuscompanion.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campuscompanion.Screen
import com.example.campuscompanion.presentation.feature.auth.signin.SignInScreen
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Navigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController();
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) "main" else Screen.LoginScreen.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.LoginScreen.route) {
            SignInScreen(navController = navController)
        }
        composable(route = "main") {
            NavigationBarExample()
        }
    }
}
