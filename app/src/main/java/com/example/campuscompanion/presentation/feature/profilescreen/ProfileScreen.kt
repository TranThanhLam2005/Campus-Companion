package com.example.campuscompanion.presentation.feature.profilescreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.campuscompanion.R
import com.example.campuscompanion.Screen

@Composable
fun ProfileScreen(modifier: Modifier = Modifier, navController: NavController) {
    var infoDialogType by remember { mutableStateOf<String?>(null) }
    var isSettingsOpen by remember { mutableStateOf(false) }

    val viewModel: ProfileViewModel = hiltViewModel()
    val userState by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var isRotated by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isRotated) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "rotation"
    )

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    } else {
        val fullName = userState?.fullName ?: ""
        val email = userState?.email ?: ""
        val contact = userState?.contact ?: ""

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            ){
                Text(
                    text = "Account",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Setting",
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(rotation)
                        .clickable {
                            isRotated = !isRotated
                            isSettingsOpen = !isSettingsOpen
                            navController.navigate(Screen.SettingScreen.route)
                        }
                )
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .padding(30.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Image(
                            painter = painterResource(id = R.drawable.account),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(60.dp)
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ){
                            Text(
                                text = fullName,
                                color = Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = email,
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                            Text(
                                text = contact,
                                color = Color.Gray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoCard(
                            icon = Icons.Outlined.Fastfood,
                            text = "Order history",
                            onClick = {
                                navController.navigate(Screen.OrderHistoryScreen.route)
                            }
                        )
                        InfoCard(
                            icon = Icons.Outlined.EventNote,
                            text = "Event history",
                            onClick = { }
                        )
                        InfoCard(
                            icon = Icons.Outlined.Payment,
                            text = "Payment history",
                            onClick = { }
                        )
                        InfoCard(
                            icon = Icons.Outlined.RateReview,
                            text = "Review",
                            onClick = { }
                        )
                    }

                    Divider(color = Color.LightGray)

                    // 📘 App Info Section
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoOption(
                            icon = Icons.Outlined.Info,
                            text = "About Us",
                            onClick = { infoDialogType = "about" }
                        )
                        InfoOption(
                            icon = Icons.Outlined.Policy,
                            text = "Privacy Policy",
                            onClick = { infoDialogType = "privacy" }
                        )
                        InfoOption(
                            icon = Icons.Outlined.Terminal,
                            text = "Terms of Service",
                            onClick = { infoDialogType = "terms" }
                        )
                        InfoOption(
                            icon = Icons.Outlined.HelpOutline,
                            text = "Help & Support",
                            onClick = { infoDialogType = "help" }
                        )
                    }
                }



                // ℹ️ Info Dialog (About / Policy / Terms / Help)
                if (infoDialogType != null) {
                    val (title, content) = when (infoDialogType) {
                        "about" -> "About Us" to "Campus Companion is a student-centered app designed to simplify campus life — from organizing events and study groups to managing your personal schedule."
                        "privacy" -> "Privacy Policy" to "We respect your privacy. Your data is securely stored and will never be shared without consent. Read the full policy on our website."
                        "terms" -> "Terms of Service" to "By using this app, you agree to abide by our community guidelines and academic integrity standards."
                        "help" -> "Help & Support" to "For support, contact us at: support@campuscompanion.com"
                        else -> "" to ""
                    }

                    AlertDialog(
                        onDismissRequest = { infoDialogType = null },
                        confirmButton = {
                            TextButton(onClick = { infoDialogType = null }) {
                                Text("OK", color = MaterialTheme.colorScheme.primary)
                            }
                        },
                        title = {
                            Text(
                                text = title,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        text = {
                            Text(
                                text = content,
                                color = Color.DarkGray,
                                fontSize = 16.sp
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        containerColor = Color.White
                    )
                }
            }
        }
    }
}



@Composable
fun InfoOption(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(icon, contentDescription = text, tint = Color.Black)
        Spacer(modifier = Modifier.size(12.dp))
        Text(text, color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Normal)
    }
}

@Composable
fun InfoCard(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Icon(icon, contentDescription = text, tint = Color.Black)
        Spacer(modifier = Modifier.size(12.dp))
        Text(text, color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Normal)
    }
}


