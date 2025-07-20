package com.example.campuscompanion.presentation.feature.profilescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.campuscompanion.R
import com.example.campuscompanion.Screen
import com.example.campuscompanion.generalUi.ButtonUI
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavController) {


    val viewModel: ProfileViewModel = hiltViewModel()
    val userState by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    if(isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    }else{
        val fullName = userState?.fullName ?: ""
        val email = userState?.email ?: ""
        val contact = userState?.contact ?: ""
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 20.dp),
        ){
            Text(
                text = "Account",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 50.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(30.dp)
                        .height(350.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Image(
                        painter = painterResource(id = R.drawable.account),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(140.dp)
                    )
                    Text(
                        text = fullName,
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ){
                        Text(
                            text = "Email: ",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = email,
                            color = Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ){
                        Text(
                            text = "Contact: ",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            contact,
                            color = Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ButtonUI(
                        text = "Logout",
                        onClick = {handleLogout(navController)},
                        icon = Icons.AutoMirrored.Outlined.Logout,
                        modifier = modifier.clip(RoundedCornerShape(30.dp))
                    )
                }
            }
        }
    }
}
fun handleLogout(navController: NavController){
    FirebaseAuth.getInstance().signOut()
    navController.navigate(Screen.LoginScreen.route) {
        popUpTo(Screen.HomeScreen.route) { inclusive = true } // clears back stack
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    val context = LocalContext.current
    ProfileScreen(navController = NavController(context))
}