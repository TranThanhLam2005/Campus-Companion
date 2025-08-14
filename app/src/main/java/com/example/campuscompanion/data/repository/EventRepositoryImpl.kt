package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.repository.EventRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
): EventRepository {

    override suspend fun getEventById(clubId: String, eventId: String): Event {
        val eventSnapshot = firestore.collection("clubs")
            .document(clubId)
            .collection("events")
            .document(eventId)
            .get()
            .await()

        return eventSnapshot.toObject(Event::class.java)?.copy(id = eventSnapshot.id)
            ?: throw Exception("Event not found")
    }
}