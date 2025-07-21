package com.example.campuscompanion

sealed class Screen (val route: String){
    object LoginScreen: Screen(route = "login_screen")
    object HomeScreen: Screen(route = "home_screen")
    object ClubScreen: Screen(route = "club_screen")
    object EventScreen: Screen(route = "event_screen")
    object LocketScreen: Screen(route = "locket_screen")
    object SpotScreen: Screen(route = "spot_screen")
    object ProfileScreen: Screen(route = "profile_screen")

    object ClubScreenDetail: Screen(route = "club_screen_detail")

    object CafeteriaScreen: Screen(route = "cafeteria_screen")
    object CafeteriaScreenDetail: Screen(route = "cafeteria_screen_detail")

    object StudyScreen: Screen(route = "study_screen")
    object StudyScreenDetail: Screen(route = "study_screen_detail")

    object NearbyMapScreen: Screen(route = "nearby_map_screen")
    object NearbyMapScreenDetail: Screen(route = "nearby_map_screen_detail")

}