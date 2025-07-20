package com.example.campuscompanion.presentation.feature.clubscreen

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.campuscompanion.R
import com.example.campuscompanion.Screen
import com.example.campuscompanion.domain.model.Club


@Composable
fun ClubScreen(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: ClubViewModel = hiltViewModel()
    val joinedState by viewModel.clubs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.loadJoinedClubs()
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
        val joinedClubs = joinedState

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
            ClubGrid(clubs = joinedClubs, navController= navController)
//            Spacer(modifier = Modifier.height(30.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ){
//                Text(
//                    text = "All Clubs",
//                    color = Color(0xFFE2F163),
//                    textAlign = TextAlign.Center,
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Icon(
//                    modifier = Modifier.size(30.dp),
//                    imageVector = Icons.Outlined.Search,
//                    contentDescription = "Search",
//                    tint = Color.White
//                )
//            }
//            Spacer(modifier = Modifier.height(15.dp))
            //ClubGrid(clubs = allClubs)
        }
    }
}

@Composable
fun ClubGrid(
    clubs: List<Club>,
    navController : NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ){
        items(clubs){ club ->
            ClubCard(club, navController)
        }
    }
}

@Composable
fun ClubCard(
    club: Club,
    navController: NavController
) {
    Column(
        modifier = Modifier.clickable(){
            navController.navigate(Screen.ClubScreenDetail.route + "/${club.id}")
        }
    ){
        Image(
            painter = painterResource(id = R.drawable.logisticclub),
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