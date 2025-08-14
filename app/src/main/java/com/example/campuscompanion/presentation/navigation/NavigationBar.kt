package com.example.campuscompanion.presentation.navigation

import LocketScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.campuscompanion.Screen
import com.example.campuscompanion.presentation.feature.clubscreen.ClubScreen
import com.example.campuscompanion.presentation.feature.clubscreen.ClubScreenDetail
import com.example.campuscompanion.presentation.feature.eventscreen.EventDetailScreen
import com.example.campuscompanion.presentation.feature.homescreen.HomeScreen
import com.example.campuscompanion.presentation.feature.profilescreen.ProfileScreen
import com.example.campuscompanion.presentation.feature.spotscreen.CafeteriaScreenDetail
import com.example.campuscompanion.presentation.feature.spotscreen.SpotScreen


enum class Destination(
    val route: String,
    val label: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val contentDescription: String
) {
    HOMES(Screen.HomeScreen.route, "Home", Icons.Outlined.Home, Icons.Filled.Home,"Home"),
    CLUBS(Screen.ClubScreen.route, "Clubs", Icons.Outlined.Groups, Icons.Filled.Groups,"Clubs"),
    LOCKETS(Screen.LocketScreen.route, "Lockets", Icons.Outlined.CameraAlt, Icons.Filled.CameraAlt,"Lockets"),
    SPOTS(Screen.SpotScreen.route, "Spots", Icons.Outlined.Room, Icons.Filled.Room,"Spots"),
    PROFILES(Screen.ProfileScreen.route, "Profiles", Icons.Outlined.Person,Icons.Filled.Person, "Profiles")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route
    ) {
        Destination.entries.forEach { destination ->
            composable(destination.route) {
                when (destination) {
                    Destination.HOMES -> HomeScreen(navController = navController)
                    Destination.CLUBS -> ClubScreen(navController = navController)
                    Destination.LOCKETS -> LocketScreen(navController = navController)
                    Destination.SPOTS -> SpotScreen(navController = navController)
                    Destination.PROFILES -> ProfileScreen(navController = navController)
                }
            }
        }
        composable(Screen.ClubScreenDetail.route+"/{clubId}"){
            backStackEntry ->
            val clubId = backStackEntry.arguments?.getString("clubId") ?: return@composable
            ClubScreenDetail(clubId = clubId, navController = navController)
        }
        composable(Screen.CafeteriaScreenDetail.route+"/{cafeteriaId}") {
            backStackEntry ->
            val cafeteriaId = backStackEntry.arguments?.getString("cafeteriaId") ?: return@composable
            CafeteriaScreenDetail(cafeteriaId = cafeteriaId, navController = navController)
        }

        composable(Screen.EventDetailScreen.route+ "/{clubId}/{eventId}") {
            backStackEntry ->
            val clubId = backStackEntry.arguments?.getString("clubId") ?: return@composable
            val eventId = backStackEntry.arguments?.getString("eventId") ?: return@composable

            EventDetailScreen(clubId = clubId, eventId = eventId, navController = navController)
        }
    }
}

@Composable
fun NavigationBarExample(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val startDestination = Destination.HOMES
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route)
                            selectedDestination = index
                        },
                        icon = {
                            val displayIcon = if(index == selectedDestination) destination.selectedIcon else destination.unselectedIcon
                            Icon(
                                displayIcon,
                                contentDescription = destination.contentDescription,
                                modifier = Modifier.size(40.dp)
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { contentPadding ->
        AppNavHost(navController, startDestination, modifier = Modifier.padding(contentPadding))
    }
}


