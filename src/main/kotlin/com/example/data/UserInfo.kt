package com.example.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    @SerialName("given_name") val givenName: String,
    val email: String,
    @SerialName("verified_email") val verified_email: Boolean,
    val picture: String,
    val locale: String
)