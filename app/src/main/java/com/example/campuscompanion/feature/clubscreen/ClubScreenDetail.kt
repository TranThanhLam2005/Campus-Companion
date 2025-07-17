package com.example.campuscompanion.feature.clubscreen

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuscompanion.R
import com.example.campuscompanion.feature.spotscreen.Room
import com.example.campuscompanion.feature.spotscreen.RoomCard
import com.example.campuscompanion.feature.spotscreen.RoomCardGrid
import com.example.campuscompanion.generalUi.ButtonUI


data class Event(
    val name: String,
    val dateAndTime: String,
    val painter: Int
)
@Composable
fun ClubScreenDetail(modifier: Modifier = Modifier) {
    val event = Event("Hackathon Warmup", "22 July 2025", R.drawable.event)
    val listEvent = listOf(
        Event("Hackathon Warmup", "22 July 2025", R.drawable.event),
        Event("Hackathon Warmup", "22 July 2025", R.drawable.event),
        Event("Hackathon Warmup", "22 July 2025", R.drawable.event),
        Event("Hackathon Warmup", "22 July 2025", R.drawable.event),
        Event("Hackathon Warmup", "22 July 2025", R.drawable.event),
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ){
        Row(
            modifier = Modifier.padding(top = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
            Text(
                text = "Club View",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                )
        }
        Image(
            painter = painterResource(id = R.drawable.bannerclub),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Business Club",
                color = Color.Black,
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            ButtonUI(
                text = "Follow",
                onClick = {  },
                modifier = Modifier.clip(RoundedCornerShape(30.dp))
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Business",
                color = Color.Black,
                fontSize = 18.sp,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ){
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Black,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Building 2, Room 2004",
                    color = Color.Black,
                    fontSize = 18.sp,
                )
            }
        }
        Text(
            "For Business Club, Leader is not a position but direction, in which everyone has the opportunity to learn, contribute, capture value in return.",
            color = Color.Gray,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 15.dp),
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                imageVector = Icons.Filled.LinkOff,
                contentDescription = "Location",
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = "https://www.facebook.com/RBAChampion",
                color = Color.Blue,
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline,
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                imageVector = Icons.Outlined.House,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(32.dp)
            )
            Text(
                "Upcomming Events:",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFF2F4F7)),
        ){
            EventCardGrid(
                modifier = Modifier.padding(10.dp),
                eventList = listEvent
            )
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Image(
            painter = painterResource(id = event.painter),
            contentDescription = event.dateAndTime,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

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
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = event.dateAndTime,
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
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF902A1D))
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun EventCardGrid(
    modifier: Modifier,
    eventList : List<Event>
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ){
        items(eventList){ event ->
            EventCard(event)
        }
    }
}

@Preview
@Composable
fun ClubScreenDetailPreview(){
    ClubScreenDetail()
}