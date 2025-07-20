package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.Club
import com.example.campuscompanion.domain.repository.ClubRepository
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClubRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ClubRepository {
    override suspend fun getClubsByIds(ids: List<String>): List<Club> {
        if (ids.isEmpty()) return emptyList()

        return ids.map { id ->
            firestore.collection("clubs").document(id).get().await()
        }.mapNotNull { snapshot ->
            snapshot.toObject(Club::class.java)?.copy(id = snapshot.id)
        }
    }

    override suspend fun getClubById(id: String): Club {
        return firestore.collection("clubs")
            .document(id)
            .get().await()
            .toObject(Club::class.java)!!
    }
}