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
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.PriceCheck
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.PriceCheck
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.campuscompanion.R


data class Catin(
    val name: String,
    val description: String,
    val reviewStart: Int,
    val price: Int,
    val painter: Int
)
@Composable
fun CafeteriaSection(modifier: Modifier = Modifier) {
    val list = listOf(
        Catin("Global Catin", "Rice - Chicken - Noddle", 4, 10000, R.drawable.catin),
        Catin("Global Catin", "Rice - Chicken - Noddle", 4, 10000, R.drawable.catin),
        Catin("Global Catin", "Rice - Chicken - Noddle", 4, 10000, R.drawable.catin),
        Catin("Global Catin", "Rice - Chicken - Noddle", 4, 10000, R.drawable.catin),
    )
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF2F4F7)),
    ){
        CatinCardGrid(
             catinList = list,
            modifier = Modifier.padding(10.dp)
        )
    }
}

@Composable
fun CatinCard(
    catin: Catin
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(8.dp)
    ){
        Image(
            painter = painterResource(id = catin.painter),
            contentDescription =  catin.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    RoundedCornerShape(20.dp)
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Text(
                catin.name,
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                catin.description,
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
                        "${catin.reviewStart}/5",
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
                        "${catin.price} VND",
                        color = Color.Gray,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun CatinCardGrid(
    modifier: Modifier,
    catinList: List<Catin>
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ){
        items(catinList){ catin ->
            CatinCard(catin)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun preview (){
    CafeteriaSection()
}