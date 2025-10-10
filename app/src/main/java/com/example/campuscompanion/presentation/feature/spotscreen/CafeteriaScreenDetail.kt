package com.example.campuscompanion.presentation.feature.spotscreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.campuscompanion.domain.model.Food
import com.example.campuscompanion.domain.model.FoodOrder
import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.generalUi.ButtonUI
import kotlinx.coroutines.delay



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CafeteriaScreenDetail(cafeteriaId: String, navController: NavController) {
    // collect state from view model
    val viewModel: CafeteriaDetailViewModel = hiltViewModel()
    val cafeteriaState by viewModel.cafeteria.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val lastOrderId by viewModel.lastOrderId.collectAsState()

    // Selected type of food
    var selected by remember { mutableStateOf<String?>(null) }

    // Show cart for preview choose food
    var showCart by remember { mutableStateOf(false) }

    // Order content information
    var name by remember { mutableStateOf("") }
    var takenote by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var showConfirmation by remember { mutableStateOf(false) }

    // Cart items and total
    val cartItems = remember { mutableStateListOf<FoodOrder>() }
    val total by remember {
        derivedStateOf {
            cartItems.sumOf { it.food.price * it.quantity }
        }
    }
    var isPressedTrash by remember { mutableStateOf(false) }
    // Scale animation (grow when pressed)
    val scale by animateFloatAsState(
        targetValue = if (isPressedTrash) 1.3f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "scale"
    )

    // Rotation animation (-30° to 0°)
    val rotation by animateFloatAsState(
        targetValue = if (isPressedTrash) -30f else 0f,
        animationSpec = tween(durationMillis = 150),
        label = "rotation"
    )

    // Reset animation after press
    LaunchedEffect(isPressedTrash) {
        if (isPressedTrash) {
            delay(150)
            isPressedTrash = false
        }
    }
    // helper function to add to cart
    fun addToCart(food: Food) {
        val index = cartItems.indexOfFirst { it.food.id == food.id }
        if(index != -1){
            val current = cartItems[index]
            cartItems[index] = current.copy(quantity = current.quantity + 1)
        }else{
            cartItems.add(FoodOrder(food, 1))
        }
    }

    // helper function to update quantity
    fun updateQuantity(foodId: String, change: Int) {
        val index = cartItems.indexOfFirst { it.food.id == foodId }
        if (index != -1) {
            val current = cartItems[index]
            val newQty = current.quantity + change
            if (newQty <= 0) {
                cartItems.removeAt(index)
            } else {
                cartItems[index] = current.copy(quantity = newQty)
            }
        }
    }

    // Arrow rotation
    val rotationAngle by animateFloatAsState(
        targetValue = if (expanded) 90f else 0f, // 90 degrees → down
        label = "arrowRotation"
    )

    LaunchedEffect(Unit) {
        viewModel.loadCafeteria(cafeteriaId)

    }
    LaunchedEffect(cafeteriaState) {
        cafeteriaState?.foodTypeList?.firstOrNull()?.name?.let { firstType ->
            selected = firstType
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
        val cafeteria = cafeteriaState
        Column( modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 20.dp, end = 20.dp, bottom = 110.dp, top = 60.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.clickable{
                            navController.popBackStack()
                        }
                    )
                    Text(
                        text = "Cafeteria View",
                        color = Color.Black,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    Icon(
                        imageVector = Icons.Filled.ChatBubbleOutline,
                        contentDescription = "Chat",
                        tint = Color.Black,
                        modifier = Modifier.clickable{
                            //navController.popBackStack()
                        }
                    )
                    Box(
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(onClick = { showCart = !showCart }) {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.Black,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        if (cartItems.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(Color(0xFF902A1D), shape = RoundedCornerShape(50))
                                    .align(Alignment.TopEnd),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cartItems.sumOf { it.quantity }.toString(),
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            val cleanUrl = cafeteria?.imageUrl?.trim()
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
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 20.dp),
                )
            }
            else{
                // Placeholder for loading or missing image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = cafeteria?.name ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = cafeteria?.description ?: "",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Star, contentDescription = null, tint = Color.Black)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${cafeteria?.star?: ""}/5", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth(),
            ) {
                cafeteria?.foodTypeList?.forEach { category ->
                    AssistChip(
                        onClick = { selected = category.name },
                        label = {
                            Text(
                                text = category.name,
                                color = if(selected == category.name) Color.White else Color.Black,
                                fontSize = 16.sp,
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        },
                        shape = RoundedCornerShape(50),
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (selected == category.name) Color(0xFF902A1D) else Color.White ,
                            labelColor = if (selected == category.name) Color.White else Color.Black
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
                if( cafeteria?.foodTypeList?.find { it.name == selected }?.foodList.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No food available in this category",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                }else{
                    FoodCardGrid(
                        modifier = Modifier.padding(10.dp),
                        foodList = cafeteria?.foodTypeList?.find { it.name == selected }?.foodList ?: emptyList(),
                        onAddToCart = { food ->
                            addToCart(food)
                        }
                    )
                }
            }
        }
    }
    // Bottom Sheet Content
    if (showCart) {
        ModalBottomSheet(
            onDismissRequest = { showCart = false },
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(26.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "Name",
                        fontWeight = FontWeight.Light,
                        fontSize = 20.sp
                    )
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Trash",
                        tint = Color(0xFF902A1D),
                        modifier = Modifier
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                rotationZ = rotation
                            )
                            .clickable{
                                isPressedTrash = true
                                cartItems.clear()
                                takenote = ""
                        }.size(34.dp)
                    )
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Enter full name") },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
                OutlinedTextField(
                    value = takenote,
                    onValueChange = { takenote = it },
                    placeholder = { Text("Enter note") },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ){
                        Text(
                            text = "Total:",
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "$total VND",
                            fontSize = 20.sp
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable(){
                            expanded = !expanded
                        }
                    ){
                        Text(
                            text = "Breakdown",
                            fontWeight = FontWeight.Light,
                            fontSize = 20.sp,
                            color = Color(0xFF902A1D)
                        )
                        Icon(
                            imageVector = Icons.Outlined.ArrowRight,
                            contentDescription = "Toggle breakdown",
                            tint = Color.Black,
                            modifier = Modifier.rotate(rotationAngle)
                        )
                    }
                }
                if (expanded) {
                    Box(modifier = Modifier.height(400.dp)) {
                        FoodPreviewGrid(
                            items = cartItems,
                            onIncrease = { id -> updateQuantity(id, 1) },
                            onDecrease = { id -> updateQuantity(id, -1) },
                            onDelete = { id -> updateQuantity(id,
                               -cartItems.find { it.food.id == id }?.quantity!!
                            )}
                        )
                    }
                }
                ButtonUI(
                    text ="PlaceOrder",
                    onClick = {
                        viewModel.addOrder(cafeteriaId, cartItems, "Pending", total, takenote, name)
                        showCart = false
                        showConfirmation = true
                        cartItems.clear()
                    },
                    enabled = !cartItems.isEmpty() && name.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFF902A1D),
                    textColor = Color.White,
                    fontSize = 20
                )
            }
        }
    }
    if (showConfirmation) {
        AlertDialog(
            onDismissRequest = { showConfirmation = false },
            confirmButton = {
                TextButton(onClick = { showConfirmation = false }) {
                    Text(
                        text = "OK",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF902A1D)
                    )
                }
            },
            title = {
                Text(
                    text = "Order Placed Successfully 🎉",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.Black
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Background circle for icon
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFFE8F5E9), shape = RoundedCornerShape(32.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Text(
                        text = "Order #${lastOrderId}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Text(
                        text = "Please keep that number!\nYou’ll receive a notification when your food is ready.",
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White
        )
    }
}

@Composable
fun FoodCard(food: Food, onAddToCart:(Food) -> Unit) {

    var isPressed by remember { mutableStateOf(false) }
    LaunchedEffect(isPressed) {
        delay(150)
        isPressed = false
    }
    // 👇 Animate the scale (1f → 1.3f → 1f)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.3f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "scale"
    )
    val cleanUrl = food.imageUrl?.trim()
    val context = LocalContext.current
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(cleanUrl)
            .crossfade(true)
            .build()
    )
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .fillMaxWidth()
    ) {
        if(!cleanUrl.isNullOrBlank()){
            Image(
                painter = painter,
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
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
                text = "${food.price} VND",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = "Add",
                tint = Color.Black,
                modifier = Modifier
                    .size(20.dp)
                    .scale(scale)
                    .clickable(){
                        isPressed = true
                        onAddToCart(food)
                    }
            )
        }
    }
}
@Composable
fun FoodCardGrid(
    foodList: List<Food>,
    modifier: Modifier,
    onAddToCart: (Food) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        items(foodList) { food ->
            FoodCard(food = food, onAddToCart = onAddToCart)
        }
    }
}


@Composable
fun FoodPreview(
    foodOrder: FoodOrder,
    onIncrease: () -> Unit = {},
    onDecrease: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF9F9F9))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Title Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = foodOrder.food.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = Color(0xFF902A1D),
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onDelete() }
            )
        }

        // Price
        Text(
            text = "${foodOrder.food.price} VND",
            fontSize = 16.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )

        // Quantity Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEFEFEF))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Remove,
                contentDescription = "Decrease",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDecrease() },
                tint = Color.Black
            )
            Text(
                text = foodOrder.quantity.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector = Icons.Outlined.AddCircle,
                contentDescription = "Increase",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onIncrease() },
                tint = Color.Black
            )
        }
    }
}

@Composable
fun FoodPreviewGrid(
    items: List<FoodOrder>,
    onIncrease: (String) -> Unit,
    onDecrease: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items) { order ->
            FoodPreview(
                foodOrder = order,
                onIncrease = { onIncrease(order.food.id) },
                onDecrease = { onDecrease(order.food.id) },
                onDelete = { onDelete(order.food.id) }
            )
        }
    }
}
