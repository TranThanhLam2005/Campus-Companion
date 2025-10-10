package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.model.Food
import com.example.campuscompanion.domain.model.FoodType
import com.example.campuscompanion.domain.repository.CafeteriaRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CafeteriaRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): CafeteriaRepository {
    override suspend fun getAllCafeterias(): List<Cafeteria> {
        val snapshot = firestore.collection("cafeterias").get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Cafeteria::class.java)?.copy(id = doc.id)
        }
    }

    override suspend fun getCafeteriaById(id: String): Cafeteria = coroutineScope {
        val snapshot = firestore.collection("cafeterias").document(id)
        val cafeteriaDoc = snapshot.get().await()
        val cafeteria = cafeteriaDoc.toObject(Cafeteria::class.java)?.copy(id = id)
            ?: throw Exception("Cafeteria not found")

        val typesSnapshot = snapshot.collection("types").get().await()

        val foodTypes = typesSnapshot.documents.map { typeDoc ->
            val type = typeDoc.getString("type") ?: "Unknown"
            val typeId = typeDoc.id

            val foodsSnapshot = snapshot.collection("types")
                .document(typeId)
                .collection("foods")
                .get()
                .await()

            val foods = foodsSnapshot.documents.mapNotNull { foodDoc ->
                foodDoc.toObject(Food::class.java)?.copy(id = foodDoc.id)
            }

            FoodType(id = typeId, name = type, foodList = foods)
        }

        cafeteria.copy(foodTypeList = foodTypes)
    }
}