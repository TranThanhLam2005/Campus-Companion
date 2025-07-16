package com.example.campuscompanion.feature.clubscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campuscompanion.R

data class Club(
    val name: String,
    val painter: Int
)
@Composable
fun ClubScreen(modifier: Modifier = Modifier, navController: NavController) {
    val joinedClubs = listOf(
        Club("Debate Club", R.drawable.logisticclub),
        Club("Music Club", R.drawable.logisticclub),
        Club("Debate Club", R.drawable.logisticclub),
        Club("Music Club", R.drawable.logisticclub)
    )
    val allClubs = listOf(
        Club("Debate Club", R.drawable.logisticclub),
        Club("Music Club", R.drawable.logisticclub),
        Club("Debate Club", R.drawable.logisticclub),
        Club("Music Club", R.drawable.logisticclub),
        Club("Music Club", R.drawable.logisticclub),
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(30.dp)
    ){
        Text(
            text = "Joined Clubs",
            color = Color(0xFFE2F163),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
        )
        ClubGrid(clubs = joinedClubs)
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "All Clubs",
                color = Color(0xFFE2F163),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Outlined.Search,
                contentDescription = "Search",
                tint = Color.White
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        ClubGrid(clubs = allClubs)
    }
}

@Composable
fun ClubGrid(
    clubs: List<Club>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ){
        items(clubs){ club ->
            ClubCard(club)
        }
    }
}

@Composable
fun ClubCard(
    club: Club
) {
    Column(){
        Image(
            painter = painterResource(id = club.painter),
            contentDescription = club.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(140.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = club.name,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ClubScreenPreview(){
    val context = LocalContext.current
    ClubScreen(navController = NavController(context))
}