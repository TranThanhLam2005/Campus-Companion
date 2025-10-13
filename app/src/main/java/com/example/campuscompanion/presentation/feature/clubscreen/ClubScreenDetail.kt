package com.example.campuscompanion.presentation.feature.clubscreen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.campuscompanion.Screen
import com.example.campuscompanion.domain.model.Club
import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.generalUi.ButtonUI
import com.example.campuscompanion.presentation.feature.spotscreen.formatTimestamp
import com.google.firebase.auth.FirebaseAuth



@Composable
fun ClubScreenDetail(modifier: Modifier = Modifier, clubId: String, navController: NavController) {

    val viewModel: ClubDetailViewModel = hiltViewModel()
    val clubState by viewModel.club.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current
    var isFollow by remember { mutableStateOf(false) }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    LaunchedEffect(Unit) {
        viewModel.loadClub(clubId)
    }

    LaunchedEffect(clubState) {
        // Check if the current user is following the club
        if (clubState != null) {
            isFollow = clubState!!.memberList.contains(currentUserId)
        }
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
        val club = clubState
        club?.let { club ->
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(bottom = 120.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    // Header row with back button
                    Row(
                        modifier = Modifier.padding(top = 60.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.clickable {
                                navController.popBackStack()
                            }
                        )
                        Text(
                            text = "Club View",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                item {
                    val cleanUrl = club.imageUrl?.trim()
                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(cleanUrl)
                            .crossfade(true)
                            .build()
                    )
                    if(!cleanUrl.isNullOrBlank()) {
                        Image(
                            painter = painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(top = 20.dp)
                                .clip(RoundedCornerShape(20.dp))
                        )
                    } else {
                        // Placeholder for loading or missing image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color.LightGray)
                        )
                    }

                }

                item {
                    // Club name + button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = club.name,
                            color = Color.Black,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        ButtonUI(
                            text = if (isFollow) "Unfollow" else "Follow",
                            backgroundColor = if (isFollow) Color.LightGray else Color(0xFF902A1D), // Unfollow = gray, Follow = red
                            textColor = if (isFollow) Color.Black else Color.White,
                            onClick = {
                                if (isFollow) {
                                    viewModel.unFollowClub(clubId, currentUserId)
                                } else {
                                    viewModel.followClub(clubId, currentUserId)
                                }
                                isFollow = !isFollow // Toggle follow state
                            },
                            modifier = Modifier.clip(RoundedCornerShape(50.dp))
                        )
                    }
                }

                item {
                    // Club type and location
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = club.type,
                            color = Color.Black,
                            fontSize = 18.sp,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.LocationOn,
                                contentDescription = club.location,
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                            Text(
                                text = club.location,
                                color = Color.Black,
                                fontSize = 18.sp,
                            )
                        }
                    }
                }

                item {
                    Text(
                        club.description,
                        color = Color.Gray,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 15.dp),
                    )
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LinkOff,
                            contentDescription = "Location",
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = club.socialLink,
                            color = Color.Blue,
                            fontSize = 16.sp,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable {
                                val socialLink = club.socialLink
                                val socialForApp = Uri.parse("fb://facewebmodal/f?href=$socialLink")

                                val facebookIntent = Intent(Intent.ACTION_VIEW, socialForApp)

                                try {
                                    context.startActivity(facebookIntent)
                                }catch(e: ActivityNotFoundException) {
                                    // If the Facebook app is not installed, open in browser
                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(socialLink))
                                    context.startActivity(browserIntent)
                                }
                            }
                        )
                    }
                }

                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.House,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            "Upcoming Events:",
                            color = Color.Black,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if(club.events.isEmpty()){
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No upcoming events",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }else{
                    // 📌 Events list
                    items(club.events) { event ->
                        EventCard(event = event, navController= navController, club = club)
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event, navController: NavController, club: Club) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable{
                navController.navigate(Screen.EventDetailScreen.route + "/${club.id}/${event.id}"
                )
            }
    ) {
        val cleanUrl = event.imageUrl?.trim()
        val context = LocalContext.current
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(cleanUrl)
                .crossfade(true)
                .build()
        )
        if(!cleanUrl.isNullOrBlank() ) {
            Image(
                painter = painter,
                contentDescription = event.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("No Image", color = Color.White)
            }
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 300f
                    )
                )
        )

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.name,
                    color = Color(0xFFE2F163),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                val formatted = formatTimestamp(context, event.date)

                Text(
                    formatted,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notification",
                tint = Color.White,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF902A1D))
                    .padding(8.dp)
            )
        }
    }
}
