package com.example.campuscompanion.presentation.feature.spotscreen

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
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PriceCheck
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.campuscompanion.R
import com.example.campuscompanion.Screen
import com.example.campuscompanion.domain.model.Cafeteria


@Composable
fun CafeteriaSection(modifier: Modifier = Modifier, navController: NavController) {

    val viewModel: CafeteriaViewModel = hiltViewModel()
    val cafeteriasState by viewModel.cafeterias.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCafeterias()
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
    }else {
        val cafeterias = cafeteriasState
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFF2F4F7))
                .padding(bottom = 80.dp),
        ){
            CafeteriaCardGrid(
                cafeteriaList = cafeterias,
                navController = navController,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun CafeteriaCard(
    cafeteria: Cafeteria,
    navController: NavController,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(8.dp)
    ){
        Image(
            painter = painterResource(id = R.drawable.catin),
            contentDescription =  cafeteria.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(20.dp)
                )
                .clickable(){
                    navController.navigate(Screen.CafeteriaScreenDetail.route + "/${cafeteria.id}")
                }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Text(
                cafeteria.name,
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(){
                    navController.navigate(Screen.CafeteriaScreenDetail.route + "/${cafeteria.id}")
                }
            )
            Text(
                cafeteria.title,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ){
                    Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = Color.Black)
                    Text(
                        "${cafeteria.star}/5",
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ){
                    Icon(imageVector = Icons.Filled.Payment, contentDescription = null, tint = Color.Black)
                    Text(
                        "${cafeteria.price} VND",
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun CafeteriaCardGrid(
    modifier: Modifier,
    cafeteriaList: List<Cafeteria>,
    navController: NavController
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ){
        items(cafeteriaList){ cafeteria ->
            CafeteriaCard(cafeteria, navController)
        }
    }
}

