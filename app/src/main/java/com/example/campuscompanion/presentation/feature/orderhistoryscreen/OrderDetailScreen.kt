package com.example.campuscompanion.presentation.feature.orderhistoryscreen

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.campuscompanion.R
import com.example.campuscompanion.domain.model.Order
import java.text.SimpleDateFormat
import java.util.Locale

// Import status enum for consistent codes
import com.example.campuscompanion.presentation.feature.orderhistoryscreen.OrderStatus
import com.example.campuscompanion.Screen

@Composable
fun OrderDetailScreen(
    orderId: String,
    navController: NavController,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {
    val order by viewModel.order.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Load the order once
    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    order?.let { order ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
                .padding(top = 60.dp, bottom = 110.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        modifier = Modifier
                            .clickable { navController.popBackStack() }
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(R.string.order_detail),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Summary
            item {
                OrderSummaryCard(order)
            }

            // Info section
            item {
                OrderInfoCard(order)
            }

            // Buttons (dynamic by status)
            item {
                ActionButtons(
                    status = order.status,
                    onCancel = { viewModel.cancelOrder(order.id) },
                    onReorder = {
                        viewModel.reorder(order) { newId ->
                            navController.navigate(Screen.OrderDetailScreen.route + "/$newId")
                        }
                    }
                )
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(stringResource(R.string.no_order), color = Color.Gray)
    }
}

@Composable
fun OrderSummaryCard(order: Order) {
    // Calculate totalDish correctly
    val totalDish = remember(order) { order.foodOrderList.sumOf { it.quantity } }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(stringResource(R.string.order_summary), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            order.foodOrderList.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = item.food.imageUrl,
                            contentDescription = item.food.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${item.quantity} x ${item.food.name}")
                    }
                    Text("${item.food.price * item.quantity}đ", fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider()
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(stringResource(R.string.total_dish, totalDish), fontWeight = FontWeight.SemiBold)
                Text("${order.totalPrice}đ", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun OrderInfoCard(order: Order) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(stringResource(R.string.order_information), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(stringResource(R.string.note), order.note.ifEmpty { stringResource(R.string.none) })
            InfoRow(stringResource(R.string.order_id), order.id)
            val formattedDate = order.orderedAt?.toDate()?.let {
                SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault()).format(it)
            } ?: stringResource(R.string.unknown)
            InfoRow(stringResource(R.string.order_date), formattedDate)
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Text(value, textAlign = TextAlign.End)
    }
}

@Composable
fun ActionButtons(
    status: String,
    onCancel: () -> Unit,
    onReorder: () -> Unit
) {
    var showCancelDialog by remember { mutableStateOf(false) }
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text(stringResource(R.string.cancel_order)) },
            text = { Text(stringResource(R.string.cancel_order_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    showCancelDialog = false
                    onCancel()
                }) {
                    Text(stringResource(R.string.yes), color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }

    when (status) {
        OrderStatus.PENDING.code -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.cancel_order),
                    color = Color.Red,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { showCancelDialog = true }
                )
            }
        }
        OrderStatus.PROCESSING.code -> {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    modifier = Modifier
                        .scale(scale),
                    tint = Color(0xFF044409)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.status_processing),
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF044409)
                )
            }
        }
        OrderStatus.DONE.code, OrderStatus.CANCELLED.code -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.reorder),
                    color = Color(0xFF074F0B),
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onReorder() }
                )
            }
        }
        else -> {
            // No actions for unknown status
        }
    }
}
