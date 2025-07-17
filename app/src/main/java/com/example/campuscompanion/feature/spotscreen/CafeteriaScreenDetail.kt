package com.example.campuscompanion.feature.spotscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.campuscompanion.feature.clubscreen.EventCardGrid

data class Food(
    val name: String,
    val price: String,
    val painter: Int
)


@Composable
fun CafeteriaScreenDetail() {
    val selected = remember { mutableStateOf("Burger") }
    val foodList = listOf(
        Food("Burger Ferguson", "40.000 VND", R.drawable.burger),
        Food("Burger Ferguson", "40.000 VND", R.drawable.burger),
        Food("Burger Ferguson", "40.000 VND", R.drawable.burger),
        Food("Burger Ferguson", "40.000 VND", R.drawable.burger)
    )

    Column( modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp)
    ) {
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
            painter = painterResource(id = R.drawable.catin), // Replace with your header image
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(top = 20.dp),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Global Catin",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Global catin provide asian food that appropriate for all student from different culture from rice to sandwich",
                fontSize = 18.sp,
                color = Color.Gray
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(4.dp))
                Text("4.7/5", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth(),
        ) {
            listOf("Sandwich", "Burger", "Hotdog", "Pizza").forEach { category ->
                AssistChip(
                    onClick = { selected.value = category },
                    label = {
                        Text(
                            text = category,
                            color = if(selected.value == category) Color.White else Color.Black,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    },
                    shape = RoundedCornerShape(50),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selected.value == category) Color(0xFF902A1D) else Color.White ,
                        labelColor = if (selected.value == category) Color.White else Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFF2F4F7)),
        ){
            FoodCardGrid(
                modifier = Modifier.padding(10.dp),
                foodList = foodList
            )
        }
    }
}

@Composable
fun FoodCard(food: Food) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = food.painter),
            contentDescription = food.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = food.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = food.price,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = "Add",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
@Composable
fun FoodCardGrid(
    foodList: List<Food>,
    modifier: Modifier
) {
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(foodList) { food ->
            FoodCard(food = food)
        }
    }
}


@Preview
@Composable
fun CafeteriaScreenDetailPreview() {
    CafeteriaScreenDetail()
}