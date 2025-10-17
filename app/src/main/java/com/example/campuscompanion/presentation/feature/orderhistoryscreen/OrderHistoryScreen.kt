package com.example.campuscompanion.presentation.feature.orderhistoryscreen

import androidx.annotation.StringRes
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.campuscompanion.R
import com.example.campuscompanion.Screen
import com.example.campuscompanion.presentation.feature.settingscreen.ChatDialog
import java.text.SimpleDateFormat
import java.util.Locale


enum class OrderStatus(val code: String, @StringRes val titleRes: Int) {
    PENDING("Pending", R.string.status_pending),
    PROCESSING("Processing", R.string.status_processing),
    DONE("Done", R.string.status_done),
    CANCELLED("Cancelled", R.string.status_cancelled)
}
@Composable
fun OrderHistoryScreen(
    navController: NavController
) {
    val viewModel: OrderHistoryViewModel = hiltViewModel()
    var isChatBoxOpen by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(OrderStatus.PENDING) }
    val ordersWithCafeteria by viewModel.ordersWithCafeteria.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    // ✅ Trigger when 'selected' changes
    LaunchedEffect(selected) {
        viewModel.loadOrders(selected.code)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
            .padding(top = 60.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
                Text(
                    text = stringResource(R.string.order_history),
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Icon(
                imageVector = Icons.Filled.ChatBubbleOutline,
                contentDescription = stringResource(R.string.chat),
                tint = Color.Black,
                modifier = Modifier.clickable {
                    isChatBoxOpen = true
                }
            )
            // 🗨️ Chat Dialog
            if (isChatBoxOpen) {
                ChatDialog(
                    onClose = { isChatBoxOpen = false },
                    message = message,
                    onMessageChange = { message = it },
                    onSend = { message = "" }
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 10.dp),
        ) {
            OrderStatus.entries.forEach { status ->
                AssistChip(
                    onClick = {
                        selected = status
                    },
                    label = {
                        Text(
                            text = stringResource(status.titleRes),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    },
                    shape = RoundedCornerShape(50),
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (selected == status) Color(0xFF902A1D) else Color.White,
                        labelColor = if (selected == status) Color.White else Color.Black
                    )
                )
            }
        }
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
        // ✅ Orders Section
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.loading_orders))
                }
            }
            ordersWithCafeteria.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.no_orders_found))
                }
            }
            else -> {
                val filteredOrders = ordersWithCafeteria
                if (filteredOrders.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(stringResource(R.string.no_status_orders_found, stringResource(selected.titleRes).lowercase()))
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 16.dp,
                            bottom = 120.dp
                        )
                    ) {
                        items(filteredOrders.size) { index ->
                            CardOrder(
                                ordersWithCafeteria = filteredOrders[index],
                                onCancelled = {
                                    viewModel.updateOrderStatus(
                                        filteredOrders[index].order.id,
                                        OrderStatus.CANCELLED.code
                                    )
                                    // 🔁 Refresh orders
                                    viewModel.loadOrders(selected.code)
                                },
                                onSeeDetail = {
                                    navController.navigate(Screen.OrderDetailScreen.route + "/${filteredOrders[index].order.id}")
                                },
                                onReorder = {
                                    viewModel.reorder(filteredOrders[index].order) { newId ->
                                        navController.navigate(Screen.OrderDetailScreen.route + "/$newId")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardOrder(
    ordersWithCafeteria: OrderWithCafeteria,
    onCancelled: () -> Unit,
    onSeeDetail: () -> Unit = {},
    onReorder: () -> Unit = {},
    onReview: () -> Unit = {}
) {
    var showCancelDialog by remember { mutableStateOf(false) }

    // 🔔 Confirmation popup when cancelling
    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text(stringResource(R.string.cancel_order)) },
            text = { Text(stringResource(R.string.cancel_order_confirm)) },
            confirmButton = {
                TextButton(onClick = {
                    showCancelDialog = false
                    onCancelled()
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

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // 🏪 Header
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ordersWithCafeteria.cafeteria?.name?.let {
                Text(
                    text = it.ifBlank { stringResource(R.string.unknown_restaurant) },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
            Text(
                text = ordersWithCafeteria.order.status,
                fontSize = 14.sp,
                color = when (ordersWithCafeteria.order.status) {
                    (OrderStatus.PENDING.code) -> Color(0xFFFF9800)
                    (OrderStatus.PROCESSING.code) -> Color(0xFF044409)
                    (OrderStatus.DONE.code) -> Color(0xFF902A1D)
                    (OrderStatus.CANCELLED.code) -> Color.Red
                    else -> Color.Black
                },
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        val cleanUrl = ordersWithCafeteria.cafeteria?.imageUrl?.trim()
        val context = LocalContext.current
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(context)
                .data(cleanUrl)
                .crossfade(true)
                .build()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (!cleanUrl.isNullOrBlank()) {
                Image(
                    painter = painter,
                    contentDescription = ordersWithCafeteria.cafeteria?.description ?: "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(140.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable { onSeeDetail() }
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFE0E0E0))
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                val formattedDate = ordersWithCafeteria.order.orderedAt?.toDate()?.let {
                    SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault()).format(it)
                } ?: stringResource(R.string.unknown)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ){
                    Text(stringResource(R.string.order_at),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Text(" $formattedDate",
                        color = Color.Gray,
                        fontSize = 14.sp,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.quantity), color = Color.Gray)
                    Text("x${ordersWithCafeteria.order.foodOrderList.sumOf { it.quantity }}", fontWeight = FontWeight.Medium)
                }
                Text(
                    stringResource(R.string.note_colon, ordersWithCafeteria.order.note.ifBlank { stringResource(R.string.no_note) }),
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Divider(
                    color = Color.LightGray.copy(alpha = 0.5f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.total_price),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text("${ordersWithCafeteria.order.totalPrice} ${stringResource(R.string.vnd)}", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🧾 Dynamic Action Buttons
        when (ordersWithCafeteria.order.status) {
            (OrderStatus.PENDING.code) -> {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.cancel_order),
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { showCancelDialog = true }
                    )
                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .height(20.dp)
                            .width(1.dp)
                    )
                    Text(
                        text = stringResource(R.string.see_detail),
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onSeeDetail() }
                    )
                }
            }
            (OrderStatus.PROCESSING.code) -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.see_detail),
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onSeeDetail() }
                    )
                }
            }
            (OrderStatus.DONE.code) -> {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.reorder),
                        color = Color(0xFF074F0B),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onReorder() }
                    )
                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .height(20.dp)
                            .width(1.dp)
                    )
                    Text(
                        text = stringResource(R.string.review),
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onReview() }
                    )
                }
            }
            (OrderStatus.CANCELLED.code) -> {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.reorder),
                        color = Color(0xFF074F0B),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onReorder() }
                    )
                    Divider(
                        color = Color.LightGray,
                        modifier = Modifier
                            .height(20.dp)
                            .width(1.dp)
                    )
                    Text(
                        text = stringResource(R.string.see_detail),
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onSeeDetail() }
                    )
                }
            }
        }
    }
}
