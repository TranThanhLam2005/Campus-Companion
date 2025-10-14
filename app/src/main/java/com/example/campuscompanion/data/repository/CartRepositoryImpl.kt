package com.example.campuscompanion.data.repository
import com.example.campuscompanion.data.local.dao.CartDao
import com.example.campuscompanion.data.local.entity.FoodCartEntity
import com.example.campuscompanion.domain.model.Food
import com.example.campuscompanion.domain.model.FoodOrder
import com.example.campuscompanion.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun observeCart(userId: String, cafeteriaId: String): Flow<List<FoodOrder>> {
        return cartDao.observeCart(userId, cafeteriaId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun addOrIncrement(userId: String, cafeteriaId: String, food: Food, delta: Int) {
        val currentQty = cartDao.getQuantity(userId, cafeteriaId, food.id) ?: 0
        val newQty = currentQty + delta
        if (newQty <= 0) {
            if (currentQty > 0) cartDao.deleteItem(userId, cafeteriaId, food.id)
            return
        }
        val entity = FoodCartEntity(
            userId = userId,
            cafeteriaId = cafeteriaId,
            foodId = food.id,
            foodName = food.name,
            price = food.price,
            imageUrl = food.imageUrl,
            quantity = newQty
        )
        cartDao.upsert(entity)
    }

    override suspend fun setQuantity(userId: String, cafeteriaId: String, foodId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteItem(userId, cafeteriaId, foodId)
        } else {
            cartDao.updateQuantity(userId, cafeteriaId, foodId, quantity)
        }
    }

    override suspend fun clearCart(userId: String, cafeteriaId: String) {
        cartDao.clearCart(userId, cafeteriaId)
    }

    private fun FoodCartEntity.toDomain(): FoodOrder = FoodOrder(
        food = Food(
            id = foodId,
            name = foodName,
            price = price,
            imageUrl = imageUrl
        ),
        quantity = quantity
    )
}
