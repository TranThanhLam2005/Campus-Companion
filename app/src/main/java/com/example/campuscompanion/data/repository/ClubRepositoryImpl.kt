package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.Club
import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.repository.ClubRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ClubRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : ClubRepository {
    override suspend fun getClubsByIds(ids: List<String>): List<Club> = coroutineScope {
        if (ids.isEmpty()) return@coroutineScope emptyList()

        val deferredSnapshots = ids.map { id ->
            async {
                firestore.collection("clubs").document(id).get().await()
            }
        }

        deferredSnapshots.awaitAll()
            .mapNotNull { snapshot ->
                snapshot.toObject(Club::class.java)?.copy(id = snapshot.id)
            }
    }

    override suspend fun getClubById(id: String): Club {
        val clubSnapshot = firestore.collection("clubs").document(id).get().await()
        val club = clubSnapshot.toObject(Club::class.java)?.copy(id = clubSnapshot.id)
            ?: throw Exception("Club not found")
        val eventsSnapshot = firestore.collection("clubs")
            .document(id)
            .collection("events")
            .get()
            .await()

        val events = eventsSnapshot.documents.mapNotNull { eventDoc ->
            eventDoc.toObject(Event::class.java)?.copy(id = eventDoc.id)
        }

        return club.copy(events = events)
    }
}