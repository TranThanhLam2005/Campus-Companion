package com.example.campuscompanion.presentation.feature.clubscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.campuscompanion.R
import com.example.campuscompanion.Screen
import com.example.campuscompanion.domain.model.Club

data class ClubType(
    val code: String, // for logic
    val titleRes: Int // for display
)
@Composable
fun ClubScreen(modifier: Modifier = Modifier, navController: NavController) {
    val viewModel: ClubViewModel = hiltViewModel()
    val joinedState by viewModel.clubs.collectAsState()
    val remainingState by viewModel.remainingClubs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val clubTypes = listOf(
        ClubType("All", R.string.club_type_all),
        ClubType("Academic", R.string.club_type_academic),
        ClubType("Social", R.string.club_type_social),
        ClubType("Sport", R.string.club_type_sport),
        ClubType("Business", R.string.club_type_business)
    )
    var isSearching by remember {mutableStateOf(false)}
    var searchQuery by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(clubTypes.first()) }
    var typeDropdownExpanded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.loadJoinedClubs()
        viewModel.loadRemainingClubs()
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
        val allClubs = remainingState
        val filteredClubs = allClubs.filter { club ->
            val matchesQuery = club.name.contains(searchQuery, ignoreCase = true)
            val matchesType = selectedType.code == "All" || club.type == selectedType.code
            matchesQuery && matchesType
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(30.dp)
        ){
            Text(
                text = stringResource(R.string.joined_clubs),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )
            ClubGrid(clubs = joinedClubs, navController= navController, modifier = Modifier.height(300.dp))
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(R.string.all_clubs),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clickable{
                            isSearching = !isSearching
                            if (isSearching) {
                                searchQuery = "" // Clear search query when starting search
                            }
                        },
                    imageVector = Icons.Outlined.Search,
                    contentDescription = stringResource(R.string.search),
                    tint = Color.White,

                )
            }
            if (isSearching) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text(stringResource(R.string.search_placeholder), color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 30.dp),
                        shape = RoundedCornerShape(24.dp),
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Black,
                            unfocusedIndicatorColor = Color.Black
                        )
                    )
                    Box {
                        OutlinedButton(
                            modifier = Modifier
                                .width(120.dp)
                                .height(50.dp),
                            onClick = { typeDropdownExpanded = true },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text(stringResource(selectedType.titleRes), color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }

                        DropdownMenu(
                            expanded = typeDropdownExpanded,
                            onDismissRequest = { typeDropdownExpanded = false }
                        ) {
                            clubTypes.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(type.titleRes)) },
                                    onClick = {
                                        selectedType = type
                                        typeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

            }
            Spacer(modifier = Modifier.height(15.dp))
            ClubGrid(clubs = filteredClubs, navController = navController,modifier = Modifier.padding(bottom = 30.dp))
        }
    }
}

@Composable
fun ClubGrid(
    clubs: List<Club>,
    navController : NavController,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 44.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier// Adjust height as needed
    ){
        items(clubs){ club ->
            ClubCard(club, navController)
        }
    }
}

@Composable
fun ClubCard(
    club: Club,
    navController: NavController,
) {
    val cleanUrl = club.imageUrl?.trim()
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(cleanUrl)
            .crossfade(true)
            .build()
    )
    Column(
        modifier = Modifier.clickable(){
            navController.navigate(Screen.ClubScreenDetail.route + "/${club.id}")
        }
    ){
        if(!cleanUrl.isNullOrBlank()){
            Image(
                painter = painter,
                contentDescription = club.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(140.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }else{
            // Placeholder for loading or missing image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
            )
        }

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
