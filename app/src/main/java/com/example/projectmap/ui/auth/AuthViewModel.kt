package com.example.projectmap.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    val currentUser get() = auth.currentUser

    fun register(
        email: String,
        pass: String,
        pin: String,
        name: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        _isLoading.value = true
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    val user = authTask.result?.user
                    if (user != null) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()

                        user.updateProfile(profileUpdates).addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {

                                val userData = hashMapOf(
                                    "email" to email,
                                    "name" to name,
                                    "security_pin" to pin
                                )

                                db.collection("users").document(user.uid).set(userData)
                                    .addOnSuccessListener {
                                        _isLoading.value = false
                                        onSuccess()
                                    }
                                    .addOnFailureListener { e ->
                                        _isLoading.value = false
                                        onError("Gagal menyimpan data database: ${e.message}")
                                    }

                            } else {
                                _isLoading.value = false
                                onError("Gagal memperbarui profil: ${profileTask.exception?.message}")
                            }
                        }
                    } else {
                        _isLoading.value = false
                        onError("Data user Firebase tidak valid")
                    }
                } else {
                    _isLoading.value = false
                    onError("Gagal Daftar: ${authTask.exception?.localizedMessage}")
                }
            }
    }
    fun login(email: String, pass: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onError(e.message ?: "Login gagal")
            }
    }

    fun verifyPin(pinInput: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            onError("User belum login")
            return
        }

        _isLoading.value = true
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                _isLoading.value = false
                if (document.exists()) {
                    val savedPin = document.getString("security_pin")
                    if (savedPin == pinInput) {
                        onSuccess()
                    } else {
                        onError("PIN salah!")
                    }
                } else {
                    onError("Data PIN tidak ditemukan")
                }
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onError(e.message ?: "Gagal memverifikasi PIN")
            }
    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onError(e.message ?: "Gagal mengirim email reset password")
            }
    }

    fun updatePin(newPin: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val userId = auth.currentUser?.uid ?: return onError("User belum login")
        _isLoading.value = true
        val userData = hashMapOf("security_pin" to newPin)
        db.collection("users").document(userId).update(userData as Map<String, Any>)
            .addOnSuccessListener {
                _isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                onError(e.message ?: "Gagal memperbarui PIN")
            }
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}