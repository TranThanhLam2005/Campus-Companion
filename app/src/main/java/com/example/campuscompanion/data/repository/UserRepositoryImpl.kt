package com.example.campuscompanion.data.repository

import androidx.compose.runtime.LaunchedEffect
import com.example.campuscompanion.domain.model.User
import com.example.campuscompanion.domain.repository.UserRepository
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
}