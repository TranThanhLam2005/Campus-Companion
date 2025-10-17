package com.example.campuscompanion.presentation.feature.eventscreen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.campuscompanion.R
import com.example.campuscompanion.domain.model.Event
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EventDetailScreen(
    clubId: String,
    eventId : String,
    navController: NavController,
    event: Event? = null // Pass the event object here
) {

    val scrollState = rememberScrollState()

    val viewModel: EventViewModel = hiltViewModel()
    val eventState by viewModel.eventDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isParticipant by viewModel.isParticipant.collectAsState()



    LaunchedEffect(Unit) {
        viewModel.loadEventDetail(clubId, eventId)
        viewModel.isParticipantInEvent(eventId)
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
        val event = eventState
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color(0xFFF6F6F6))
                .padding(start = 20.dp, end = 20.dp, bottom = 110.dp, top = 60.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.event_back),
                    tint = Color.Black,
                    modifier = Modifier.clickable{
                        navController.popBackStack()
                    }
                )
                Text(
                    text = stringResource(id = R.string.event_club_view),
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))


            // Event Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(20.dp)),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                val cleanUrl = event?.imageUrl?.trim()
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
                        contentDescription = event?.name ?: stringResource(id = R.string.event_title),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(id = R.string.event_no_image), color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Event Name
            Text(
                text = event?.name ?: stringResource(id = R.string.event_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Event Date and Location
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = stringResource(id = R.string.event_date), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatDate(event?.date, stringResource(id = R.string.event_date_unknown)),
                        color = Color.Gray
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = stringResource(id = R.string.event_location), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = event?.location ?: stringResource(id = R.string.event_location_unknown), color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = stringResource(id = R.string.event_about),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event?.description ?: stringResource(id = R.string.event_no_description),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register / Join Button
            Button(
                onClick = {
                    event?.let {
                        if(!isParticipant){
                            viewModel.participateInEvent(it.id, clubId)
                        }
                    }
                },
                enabled = !isParticipant,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if(isParticipant){
                    Text(stringResource(id = R.string.event_registered))
                }else Text(stringResource(id = R.string.event_register))
            }
        }
    }
}

@Composable
fun formatDate(timestamp: Timestamp?, unknown: String): String {
    return timestamp?.toDate()?.let {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(it)
    } ?: unknown
}