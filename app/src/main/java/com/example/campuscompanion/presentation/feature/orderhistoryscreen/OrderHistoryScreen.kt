package com.example.campuscompanion.presentation.feature.orderhistoryscreen

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.campuscompanion.Screen
import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.presentation.feature.settingscreen.ChatDialog
import com.example.campuscompanion.presentation.feature.spotscreen.SpotCategory
import java.text.SimpleDateFormat
import java.util.Locale


enum class OrderStatus (val title:String){
    PENDING("Pending"),
    PROCESSING("Processing"),
    DONE("Done"),
}

@Composable
fun OrderHistoryScreen(
    navController: NavController
){
    val viewModel: OrderHistoryViewModel = hiltViewModel()
    var isChatBoxOpen by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf(OrderStatus.PENDING) }
    val orders by viewModel.orders.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    // Load once when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadOrders()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 110.dp, top = 60.dp)
            .background(Color.LightGray),
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
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
                Text(
                    text = "Order history",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Icon(
                imageVector = Icons.Filled.ChatBubbleOutline,
                contentDescription = "Chat",
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
                .padding(bottom = 10.dp, top = 8.dp),
        ){
            OrderStatus.entries.forEach { status ->
                AssistChip(
                    onClick = { selected = status },
                    label = {
                        Text(
                            text = status.title,
                            fontSize = 16.sp,
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
        Divider(color = Color.Gray, thickness = 1.dp)
        // ✅ Orders Section
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading orders...")
                }
            }
            orders.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No orders found.")
                }
            }
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                        .padding(16.dp)
                ) {
                    items(orders.size) { index ->
                        CardOrder(order = orders[index])
                    }
                }
            }
        }
    }
}

@Composable
fun CardOrder(
    order: Order
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        // 🏪 Header: Restaurant + Status
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = order.cafeteriaId.ifBlank { "Unknown Restaurant" },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = order.status,
                fontSize = 14.sp,
                color = when (order.status) {
                    "Pending" -> Color(0xFFFF9800)
                    "Processing" -> Color(0xFF2196F3)
                    "Done" -> Color(0xFF4CAF50)
                    else -> Color.Gray
                },
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🕒 Order details
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            // 🍔 Image / placeholder
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .width(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE0E0E0))
            )

            // 📦 Order Info
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                val formattedDate = order.orderedAt?.toDate()?.let {
                    SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(it)
                } ?: "Unknown"

                Text(
                    text = "Order at: $formattedDate",
                    color = Color.DarkGray,
                    fontSize = 14.sp
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Quantity", color = Color.Gray)
                    Text("x${order.foodOrderList.sumOf { it.quantity }}", fontWeight = FontWeight.Medium)
                }
                Text("Note: ${order.note.ifBlank { "No note" }}", color = Color.Gray, fontSize = 14.sp)


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
                        text = "Total Price",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                    Text("${order.totalPrice} VND", fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🧾 Actions (inline text buttons)
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Cancel Order",
                color = Color.Red,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { /* TODO: cancel logic */ }
            )

            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .height(20.dp)
                    .width(1.dp)
            )

            Text(
                text = "Review",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { /* TODO: review logic */ }
            )
        }
    }
}
