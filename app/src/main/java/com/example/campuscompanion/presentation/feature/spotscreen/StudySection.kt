package com.example.campuscompanion.presentation.feature.spotscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.LocationOn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuscompanion.R

data class Room(
    val number: String,
    val location: String,
    val description: String,
    val availableTime: String,
    val painter: Int
)
@Composable
fun StudySection(modifier: Modifier = Modifier) {
    val room = Room("1.3006", "Building 2, Level 1","Mac lab for IT student with whiteboard","Available until 5.30pm", R.drawable.maclab)
    val List = listOf(
        Room("1.3006", "Building 2, Level 1","Mac lab for IT student with whiteboard","Available until 5.30pm", R.drawable.maclab),
        Room("1.3006", "Building 2, Level 1","Mac lab for IT student with whiteboard","Available until 5.30pm", R.drawable.maclab),
        Room("1.3006", "Building 2, Level 1","Mac lab for IT student with whiteboard","Available until 5.30pm", R.drawable.maclab),
        Room("1.3006", "Building 2, Level 1","Mac lab for IT student with whiteboard","Available until 5.30pm", R.drawable.maclab),
        Room("1.3006", "Building 2, Level 1","Mac lab for IT student with whiteboard","Available until 5.30pm", R.drawable.maclab),
    )
    RoomDetail(room)
    Spacer(modifier = Modifier.height(20.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ){
        Icon(
            imageVector = Icons.Outlined.House,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
        Text(
            "Available Rooms:",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF2F4F7))
            .padding(bottom = 80.dp)
        ,
    ){
        RoomCardGrid(
            roomList = List,
            modifier = Modifier.padding(10.dp)
        )
    }
}


@Composable
fun RoomDetail(
    room: Room
) {
    Box(){
        Image(
            painter = painterResource(id = room.painter),
            contentDescription =  room.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                )
        )
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 300f
                    )
                )
                .height(200.dp)
        ){
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.BottomStart)
            ){
                Text(
                    room.number,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    room.location,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Text(
                        room.description,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        room.availableTime,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

@Composable
fun RoomCard(
    room: Room
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(8.dp)
    ){
        Image(
            painter = painterResource(id = room.painter),
            contentDescription =  room.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(
                    RoundedCornerShape(20.dp)
                )
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ){
                Icon(imageVector = Icons.Outlined.Book, contentDescription = null, tint = Color.Gray)
                Text(
                    room.number,
                    color = Color.Gray,
                    fontSize = 18.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ){
                Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = null, tint = Color.Gray)
                Text(
                    room.location,
                    color = Color.Gray,
                    fontSize = 18.sp,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ){
                Icon(imageVector = Icons.Outlined.Description, contentDescription = null, tint = Color.Gray)
                Text(
                    room.number,
                    color = Color.Gray,
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Composable
fun RoomCardGrid(
    modifier: Modifier,
    roomList: List<Room>
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ){
        items(roomList){ room ->
            RoomCard(room)
        }
    }
}
