package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.Room
import com.example.campuscompanion.domain.repository.RoomRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : RoomRepository {
    override suspend fun getAllRooms(): List<Room> {
        val snapshot = firestore.collection("rooms").get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Room::class.java)?.copy(id = doc.id)
        }
    }

    override suspend fun getRoomById(id: String): Room {
        val snapshot = firestore.collection("rooms").document(id).get().await()
        return snapshot.toObject(Room::class.java)!!
    }

}
