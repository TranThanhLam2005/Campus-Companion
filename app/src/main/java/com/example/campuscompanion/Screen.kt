package com.example.campuscompanion

sealed class Screen (val route: String){
    object LoginScreen: Screen(route = "login_screen")
    object HomeScreen: Screen(route = "home_screen")
    object ClubScreen: Screen(route = "club_screen")
    object LocketScreen: Screen(route = "locket_screen")
    object SpotScreen: Screen(route = "spot_screen")
    object ProfileScreen: Screen(route = "profile_screen")
    object SettingScreen: Screen(route = "setting_screen")
    object OrderHistoryScreen: Screen(route = "order_history_screen")
    object OrderDetailScreen: Screen(route = "order_detail_screen")
    object EventHistoryScreen: Screen(route = "event_history_screen")

    object ClubScreenDetail: Screen(route = "club_screen_detail")
    object CafeteriaScreenDetail: Screen(route = "cafeteria_screen_detail")

    object EventDetailScreen: Screen(route = "event_detail_screen")
}