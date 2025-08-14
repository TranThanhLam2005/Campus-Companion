package com.example.campuscompanion.data.repository

import com.example.campuscompanion.domain.model.NewFeed
import com.example.campuscompanion.domain.repository.NewFeedRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class NewFeedRepositoryImpl @Inject constructor(
    private val firestore: FirebaseDatabase
): NewFeedRepository {
    override fun observeAllFeeds(): Flow<List<NewFeed>> = callbackFlow {
        val databaseRef = firestore.getReference("newfeed")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val feeds = snapshot.children.mapNotNull {
                    it.getValue(NewFeed::class.java)
                }
                trySend(feeds)
            }

            override fun onCancelled(error: DatabaseError) {
                close(Exception("Firebase error: ${error.message}"))
            }
        }

        databaseRef.addValueEventListener(listener)

        awaitClose {
            databaseRef.removeEventListener(listener)
        }
    }

    override fun addCommentToFeed(
        feedId: String,
        comment: String
    ): Flow<Unit> = callbackFlow {
        val databaseRef = firestore.getReference("newfeed")
            .child(feedId)
            .child("comment")

        val newCommentKey = databaseRef.push().key

        if (newCommentKey == null) {
            close(Exception("Failed to generate comment key"))
            return@callbackFlow
        }

        databaseRef.child(newCommentKey)
            .setValue(comment)
            .addOnSuccessListener { trySend(Unit) }
            .addOnFailureListener { close(it) }

        awaitClose { /* No cleanup needed */ }
    }
}