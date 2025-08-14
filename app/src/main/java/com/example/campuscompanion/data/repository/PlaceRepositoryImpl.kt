package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.Place
import com.example.campuscompanion.domain.repository.PlaceRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PlaceRepository{
    override suspend fun getPlaces(): List<Place> {
        val snapshot = firestore.collection("places").get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Place::class.java)?.copy(id = doc.id)
        }
    }
}