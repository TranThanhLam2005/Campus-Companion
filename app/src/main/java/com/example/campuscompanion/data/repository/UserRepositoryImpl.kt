package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.Event
import com.example.campuscompanion.domain.model.User
import com.example.campuscompanion.domain.repository.UserRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): UserRepository {
    override suspend fun getUser(): User {
        val currentUser = auth.currentUser ?: throw Exception("User not logged in")
        val uid = currentUser.uid
        val snapshot = firestore.collection("users").document(uid).get().await()
        return User(
            fullName = snapshot.getString("fullName") ?: "",
            email = snapshot.getString("email") ?: "",
            contact = snapshot.getString("contact") ?: "",
            joinedClubs = snapshot.get("joinedClubs") as? List<String> ?: emptyList()
        )
    }

    override suspend fun participantInEvent(eventId: String, clubId: String) {
        val userId = auth.currentUser?.uid ?: return
        val now = Timestamp.now()

        val userEventRef = firestore
            .collection("users")
            .document(userId)
            .collection("participating_events")
            .document(eventId)


        val data = mapOf(
            "clubId" to clubId,
            "joinedAt" to now
        )

        // Run both updates together as a batch
        userEventRef.set(data).await()
    }

    override suspend fun isParticipantInEvent(eventId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false

        val userEventRef = firestore
            .collection("users")
            .document(userId)
            .collection("participating_events")
            .document(eventId)

        val snapshot = userEventRef.get().await()
        return snapshot.exists()
    }
    override suspend fun getParticipatedEvents(): List<Event> {
        val userId = auth.currentUser?.uid ?: return emptyList()

        val userEventsRef = firestore
            .collection("users")
            .document(userId)
            .collection("participating_events")

        val snapshots = userEventsRef.get().await()
        val events = mutableListOf<Event>()

        for (doc in snapshots.documents) {
            val eventId = doc.id
            val clubId = doc.getString("clubId") ?: continue

            // Fetch event details from the events collection
            val eventSnapshot = firestore
                .collection("clubs")
                .document(clubId)
                .collection("events")
                .document(eventId)
                .get()
                .await()

            if (eventSnapshot.exists()) {
                val event = Event(
                    id = eventSnapshot.id,
                    name = eventSnapshot.getString("name") ?: "",
                    description = eventSnapshot.getString("description") ?: "",
                    date = eventSnapshot.getTimestamp("date") ?: Timestamp.now(),
                    imageUrl = eventSnapshot.getString("imageUrl") ?: "",
                    location = eventSnapshot.getString("location") ?: ""
                )
                events.add(event)
            }
        }
        return events
    }
}