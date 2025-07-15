package com.example.campuscompanion

sealed class Screen (val route: String){
    object LoginScreen: Screen(route = "login_screen")
    object MainScreen: Screen(route = "main_screen")
    object DetailScreen: Screen(route = "detail_screen")
}