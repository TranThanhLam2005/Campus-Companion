package com.example.campuscompanion.data.repository

import android.util.Log
import com.example.campuscompanion.domain.model.Order
import com.example.campuscompanion.domain.repository.OrderRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): OrderRepository {
    override suspend fun addOrder(order: Order): String {
        try {
            // Reference to "orders" collection
            val ordersCollection = firestore.collection("orders")

            // Generate a new document ID (if not already set)
            val orderId = if (order.id.isNotEmpty()) order.id else ordersCollection.document().id

            // Build the map to upload (Firestore needs simple types)
            val orderData = hashMapOf(
                "id" to orderId,
                "userId" to order.userId,
                "cafeteriaId" to order.cafeteriaId,
                "foodOrderList" to order.foodOrderList.map { foodOrder ->
                    mapOf(
                        "food" to mapOf(
                            "id" to foodOrder.food.id,
                            "name" to foodOrder.food.name,
                            "price" to foodOrder.food.price,
                            "imageUrl" to foodOrder.food.imageUrl
                        ),
                        "quantity" to foodOrder.quantity
                    )
                },
                "status" to order.status,
                "totalPrice" to order.totalPrice,
                "note" to order.note,
                "name" to order.name,
                "orderedAt" to Timestamp.now()
            )

            // Save the order
            ordersCollection.document(orderId).set(orderData).await()

            Log.d("OrderRepository", "✅ Order added successfully with ID: $orderId")

            return orderId
        } catch (e: Exception) {
            Log.e("OrderRepository", "❌ Failed to add order: ${e.message}", e)
            throw e
        }
    }

    override suspend fun getOrderDetail(orderId: String): Order {
        return try {
            val orderRef = firestore.collection("orders").document(orderId)
            val snapshot = orderRef.get().await()
            snapshot.toObject(Order::class.java) ?: throw Exception("Order not found")
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching order detail", e)
            throw e
        }
    }

    override suspend fun getOrdersByUserId(userId: String, status: String): List<Order> {
        return try {
            var query = firestore.collection("orders")
                .whereEqualTo("userId", userId)

            if (status != null) {
                query = query.whereEqualTo("status", status)
            }

            val snapshot = query.get().await()

            snapshot.documents.mapNotNull { it.toObject(Order::class.java) }
        } catch (e: Exception) {
            Log.e("OrderRepository", "Error fetching orders for user $userId with status $status", e)
            emptyList()
        }
    }


    override suspend fun updateStatus(orderId: String, newStatus: String) {
        try {
            val orderRef = firestore.collection("orders").document(orderId)
            orderRef.update("status", newStatus).await()
            Log.d("OrderRepository", "✅ Order status updated successfully")
        } catch (e: Exception) {
            Log.e("OrderRepository", "❌ Failed to update order status", e)
            throw e
        }
    }
}