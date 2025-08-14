package com.example.campuscompanion.presentation.feature.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campuscompanion.domain.model.NewFeed
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID


@Composable
fun HomeScreen(
    navController: NavController
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val latestFeeds by viewModel.feeds.collectAsState()  // Live updates
    var cachedFeeds by remember { mutableStateOf<List<NewFeed>>(emptyList()) }
    var currentTopFeed by remember { mutableStateOf<NewFeed?>(null) }

    LaunchedEffect(latestFeeds) {
        if (cachedFeeds.isEmpty()) {
            cachedFeeds = latestFeeds
            currentTopFeed = latestFeeds.lastOrNull()
        }
    }

    val comments = currentTopFeed?.comment?.values?.toList() ?: emptyList()

    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        SwipeableCardStack(
            feedList = cachedFeeds,
            onSwiped = { feed, direction ->
                println("Swiped ${feed.description} to the $direction")
                cachedFeeds = cachedFeeds.toMutableList().apply { remove(feed) }
                currentTopFeed = cachedFeeds.lastOrNull()
            },
        )


        Spacer(modifier = Modifier.height(20.dp))

        CommentSection(comments = comments)

        Spacer(modifier = Modifier.height(30.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            placeholder = { Text("Write reply...", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(24.dp),
            textStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            ),
            trailingIcon = {
                IconButton(onClick = {
                    if (comment.isNotBlank() && currentTopFeed != null) {
                        // Generate a fake Firebase push key (or UUID) locally
                        val newCommentKey = UUID.randomUUID().toString()

                        // Append the new key-value pair to the map
                        val updatedCommentMap = currentTopFeed!!.comment + (newCommentKey to comment)

                        val updatedFeed = currentTopFeed!!.copy(comment = updatedCommentMap)

                        cachedFeeds = cachedFeeds.map { feed ->
                            if (feed.id == currentTopFeed!!.id) updatedFeed else feed
                        }

                        currentTopFeed = updatedFeed
                        viewModel.addCommentToFeed(currentTopFeed!!.id, comment)
                        comment = ""
                    }
                }) {
                    Icon(Icons.Outlined.Send, contentDescription = "Send")
                }
            }

        )
    }
}


@Composable
fun NewFeed(
    newFeed: NewFeed
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(top = 90.dp)
    ) {
        // Background image
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .fillMaxSize(),
            painter = rememberAsyncImagePainter(model = newFeed.imageUrl),
            contentDescription = null,
            contentScale = ContentScale.Crop,

        )

        // Gradient overlay at bottom for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black),
                        startY = 600f
                    )
                )
        )

        // Description and location at the bottom start
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, end = 24.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = newFeed.description,
                fontSize = 18.sp,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                formatTimestamp(newFeed.createdAt),
                fontSize = 12.sp,
                color = Color.White,
                fontWeight = FontWeight.Light
            )
        }
    }
}
@Composable
fun SwipeableCardStack(
    feedList: List<NewFeed>,
    modifier: Modifier = Modifier,
    onSwiped: (NewFeed, String) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        feedList.forEachIndexed { index, feed ->
            if (index == feedList.size - 1) {
                DraggableCard(
                    feed = feed,
                    onSwiped = { direction ->
                        onSwiped(feed, direction)
                    }
                )
            } else {
                NewFeed(feed)
            }
        }
    }
}

@Composable
fun DraggableCard(
    feed: NewFeed,
    onSwiped: (String) -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val rotation = offsetX.value / 60
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .height(400.dp) // Fixed height, avoid overlapping text field
            .graphicsLayer {
                translationX = offsetX.value
                translationY = offsetY.value
                rotationZ = rotation
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        when {
                            offsetX.value > 300 -> {
                                scope.launch {
                                    offsetX.animateTo(1000f, tween(300))
                                    onSwiped("right")
                                }
                            }
                            offsetX.value < -300 -> {
                                scope.launch {
                                    offsetX.animateTo(-1000f, tween(300))
                                    onSwiped("left")
                                }
                            }
                            else -> {
                                scope.launch {
                                    offsetX.animateTo(0f, spring())
                                    offsetY.animateTo(0f, spring())
                                }
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetX.snapTo(offsetX.value + dragAmount.x)
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                        }
                    }
                )
            }
    ) {
        NewFeed(feed)
    }
}

@Composable
fun CommentSection(comments: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .heightIn(min = 150.dp, max = 250.dp)
    ) {
        Text(
            text = "Comments",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (comments.isEmpty()) {
            Text(
                text = "No comments yet. Be the first!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(comments) { comment ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        shadowElevation = 2.dp,
                        color = Color(0xFFF5F5F5),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = Color(0xFF6A6A6A),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = comment,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Just now", // You can replace with real timestamp later
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}