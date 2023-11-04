package com.example.data

data class UserSessionOAuth(
    val token: String,       // Jeton d'authentification OAuth
    val state: String        // État pour la correspondance OAuth
)
