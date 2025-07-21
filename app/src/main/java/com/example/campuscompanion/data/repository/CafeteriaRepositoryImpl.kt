package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.Cafeteria
import com.example.campuscompanion.domain.repository.CafeteriaRepository
import com.google.firebase.firestore.FirebaseFirestore
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

    override suspend fun getCafeteriaById(id: String): Cafeteria {
        val snapshot = firestore.collection("cafeterias").document(id).get().await()
        return snapshot.toObject(Cafeteria::class.java)!!
    }
}