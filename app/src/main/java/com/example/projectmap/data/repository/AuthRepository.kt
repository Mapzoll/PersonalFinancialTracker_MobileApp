package com.example.projectmap.data.repository

import com.example.projectmap.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class AuthRepository(private val isDemoMode: Boolean = false) {
    private val auth = try { FirebaseAuth.getInstance() } catch (e: Exception) { null }
    private val database = try { FirebaseDatabase.getInstance().getReference("users") } catch (e: Exception) { null }

    val currentUser get() = auth?.currentUser

    suspend fun register(email: String, password: String, username: String, pin: String): Result<User> {
        if (isDemoMode) return Result.success(User("demo_user", username, email, pin))
        return try {
            val authResult = auth?.createUserWithEmailAndPassword(email, password)?.await()
                ?: throw Exception("Firebase Auth not initialized")
            val userId = authResult.user?.uid ?: throw Exception("Registration failed")
            val user = User(userId, username, email, pin)
            database?.child(userId)?.setValue(user)?.await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<String> {
        if (isDemoMode) return Result.success("demo_user")
        return try {
            val authResult = auth?.signInWithEmailAndPassword(email, password)?.await()
                ?: throw Exception("Firebase Auth not initialized")
            val userId = authResult.user?.uid ?: throw Exception("Login failed")
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserData(userId: String): Result<User> {
        if (isDemoMode || userId == "demo_user") return Result.success(User("demo_user", "Demo User", "demo@example.com", "123456"))
        return try {
            val snapshot = database?.child(userId)?.get()?.await()
                ?: throw Exception("Firebase Database not initialized")
            val user = snapshot.getValue(User::class.java) ?: throw Exception("User not found")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth?.signOut()
    }
}
