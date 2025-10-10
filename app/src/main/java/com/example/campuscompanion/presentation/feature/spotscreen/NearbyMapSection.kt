package com.example.campuscompanion.presentation.feature.spotscreen

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.campuscompanion.R
import com.example.campuscompanion.data.repository.ApiClient
import com.example.campuscompanion.domain.model.Place
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearbyMapSection(modifier: Modifier = Modifier) {
    val viewModel: NearbyMapViewModel = hiltViewModel()
    val places by viewModel.nearbyPlaces.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        viewModel.loadNearbyPlaces()
    }
    LaunchedEffect(places){
        if (places.isNotEmpty() && selectedCategory == null) {
            selectedCategory = places.map { it.category }.distinct().sorted().firstOrNull()
        }
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
        val categories = places.map { it.category }.distinct().sorted()

        Column(modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                "Recommended Places By Alumni",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            CategorySelector(categories, selectedCategory) {
                selectedCategory = it
            }

            Spacer(modifier = Modifier.height(16.dp))
            val filteredPlaces = if (selectedCategory.isNullOrEmpty()) {
                places
            } else {
                places.filter { it.category == selectedCategory }
            }
            RecommendedPlaceList(filteredPlaces)
        }
    }

}
@Composable
fun CategorySelector(
    categories: List<String>,
    selected: String?,
    onCategorySelected: (String) -> Unit
) {
    LazyRow {
        items(categories) { category ->
            val isSelected = category == selected
            val bgColor = if (isSelected) Color(0xFF902A1D) else Color(0xFFE0E0E0)
            val textColor = if (isSelected) Color.White else Color.Black

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .clickable { onCategorySelected(category) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(category, color = textColor, fontWeight = FontWeight.Medium)
            }
        }
    }
}
@Composable
fun RecommendedPlaceList(places: List<Place>) {
    LazyColumn {
        items(places) { place ->
            PlaceCard(place)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun PlaceCard(place: Place) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF601717),
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(12.dp)
        ) {
            val cleanUrl = place.imageUrl.trim()
            val context = LocalContext.current
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(context)
                    .data(cleanUrl)
                    .crossfade(true)
                    .build()
            )
            if(!cleanUrl.isNullOrBlank()){
                // Left: Image
                Image(
                    painter = painter,
                    contentDescription = place.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(16.dp))
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
            Spacer(modifier = Modifier.width(16.dp))

            // Right: Content
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = place.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = place.description,
                    fontSize = 14.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "⭐ ${place.star}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        showDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Directions, contentDescription = "Directions", modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Show Path", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text("Path to ${place.name}", fontSize = 16.sp) },
            text = {
                Box(modifier = Modifier.height(500.dp)) {
                    // Your mini-map goes here
                    ShowDirectionMap(place)
                }
            }
        )
    }
}
@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowDirectionMap(place: Place) {
    var polylinePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }

    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    val cameraPositionState = rememberCameraPositionState()
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val locationRequest: LocationRequest = remember{
        LocationRequest.Builder(5000L)
            .setMinUpdateIntervalMillis(3000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()
    }
    val locationCallback = remember {
        object: LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let{
                        location ->
                    val latLng = LatLng(location.latitude, location.longitude)
                    currentLocation = latLng
                    coroutineScope.launch{
                        cameraPositionState.animate(
                            update = CameraUpdateFactory.newLatLngZoom(latLng, 15f),
                            durationMs = 1000
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if(permissionsState.allPermissionsGranted){
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize().weight(1f),
            cameraPositionState = cameraPositionState
            // Add your GoogleMap configuration here
        ){

            currentLocation?.let{
                Marker(
                    state = MarkerState(position = it),
                    title = "Current Location",
                )

                LaunchedEffect(currentLocation) {
                    if (currentLocation != null) {
                        polylinePoints = getPolylinePoints(
                            origin = currentLocation!!,
                            destination = LatLng(place.lat, place.long),
                            apiKey = "AIzaSyD-EEdM3Oks_LXAumGsCxAAfURC-FyFsP0"
                        )
                    }
                }
                Polyline(
                    points = polylinePoints,
                    color = Color.Blue,
                    width = 8f
                )
            }
            Marker(
                state = MarkerState(
                    position = LatLng(place.lat, place.long)
                ),
                title = place.name,
                snippet = place.description
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if(!permissionsState.allPermissionsGranted){
                Button(
                    onClick = { permissionsState.launchMultiplePermissionRequest() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF601717),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Request Location Permission", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }else{
                Text(
                    place.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF601717)
                )
            }
        }
    }
}
suspend fun getPolylinePoints(
    origin: LatLng,
    destination: LatLng,
    apiKey: String
): List<LatLng> {
    val originStr = "${origin.latitude},${origin.longitude}"
    val destStr = "${destination.latitude},${destination.longitude}"
    val response = ApiClient.api.getDirections(originStr, destStr, apiKey)
    val encodedPolyline = response.routes.firstOrNull()?.overviewPolyline?.points
    return if (encodedPolyline != null) decodePolyline(encodedPolyline) else emptyList()
}
fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if ((result and 1) != 0) (result.inv() shr 1) else (result shr 1)
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if ((result and 1) != 0) (result.inv() shr 1) else (result shr 1)
        lng += dlng

        val latitude = lat / 1E5
        val longitude = lng / 1E5
        poly.add(LatLng(latitude, longitude))
    }

    return poly
}

