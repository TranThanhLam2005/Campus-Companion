package com.example.campuscompanion.presentation.feature.spotscreen

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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.campuscompanion.R


enum class SpotCategory (val title:String){
    STUDY("Study Spots"),
    CAFE("Cafeteria"),
    MAP ("Nearby Map")
}

@Composable
fun SpotScreen(modifier: Modifier = Modifier, navController: NavController) {

    var selected by remember { mutableStateOf(SpotCategory.STUDY) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp)
        ,
    ){
        Text(
            text = "Campus Spots",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 30.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
        ){
            SpotCategory.entries.forEach { category ->
                AssistChip(
                    onClick = { selected = category },
                    label = {
                        Text(
                            text = category.title,
                            color = if(selected == category) Color.White else Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    },
                    shape = RoundedCornerShape(50), // 👈 pill-shaped corners
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selected == category) Color(0xFF902A1D) else Color.White,
                        labelColor = if (selected == category) Color.White else Color.Black
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        // Dynamic content
        when (selected) {
            SpotCategory.STUDY -> StudySection()
            SpotCategory.CAFE -> CafeteriaSection()
            SpotCategory.MAP -> NearbyMapSection()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpotScreenPreview(){
    val context = LocalContext.current
    SpotScreen(navController = NavController(LocalContext.current))
}