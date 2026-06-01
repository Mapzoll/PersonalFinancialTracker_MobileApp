package com.example.projectmap.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val securityPin: String = "" // Should be hashed in production
)
