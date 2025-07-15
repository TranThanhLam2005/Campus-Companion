package com.example.campuscompanion

sealed class Screen (val route: String){
    object LoginScreen: Screen(route = "login_screen")
    object HomeScreen: Screen(route = "home_screen")
    object ClubScreen: Screen(route = "club_screen")
    object EventScreen: Screen(route = "event_screen")
    object LocketScreen: Screen(route = "locket_screen")
    object SpotScreen: Screen(route = "spot_screen")
    object ProfileScreen: Screen(route = "profile_screen")


}