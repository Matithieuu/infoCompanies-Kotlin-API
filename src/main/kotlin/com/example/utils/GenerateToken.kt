package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.services.DAOUser
import java.util.*
import java.util.concurrent.TimeUnit


//        val secret = environment.config.property("jwt.secret").getString()
//        val issuer = environment.config.property("jwt.issuer").getString()
//        val audience = environment.config.property("jwt.audience").getString()
private val expiresAt = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10))
private const val issuer = "http://0.0.0.0:5173" //The issuer of the token (React Native App)
private const val audience = "http://127.0.0.1:8080/login" //The page where the token is sent to
private const val secret = "your-secret-key" //Encryption key
private val algorithm = Algorithm.HMAC256(secret)

fun generateToken(username: String): String {
    return JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("username", username)
        .withExpiresAt(expiresAt)
        .sign(algorithm)
}

fun generateRefreshToken(email: String): String {
    val refreshToken = JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("email", email)
        .withExpiresAt(expiresAt)
        .sign(algorithm)

    DAOUser.saveRefreshToken(email, refreshToken)
    return refreshToken
}
