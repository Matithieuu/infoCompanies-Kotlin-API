package com.example.routing


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.services.DAOUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*
import java.util.concurrent.TimeUnit

private const val BLOCK_DURATION_MINUTES = 10
private const val MAX_LOGIN_ATTEMPTS = 3
private const val MAX_REGISTER_ATTEMPTS = 3

fun Application.configureLoginRoutes() {

    fun Application.generateToken(username: String): String {
//        val secret = environment.config.property("jwt.secret").getString()
//        val issuer = environment.config.property("jwt.issuer").getString()
//        val audience = environment.config.property("jwt.audience").getString()
        val secret = "your-secret-key" //Encryption key
        val issuer = "http://0.0.0.0:5173" //The issuer of the token (React Native App)
        val audience = "http://127.0.0.1:8080/login" //The page where the token is sent to

        val subject = "Account Login API" //The subject of the token

        val algorithm = Algorithm.HMAC256(secret)
        val expiresAt = Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10))

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", username)
            .withExpiresAt(expiresAt)
            .sign(algorithm)
    }

    fun Application.generateRefreshToken(email: String): String {
        val refreshTokenExpirationMillis = TimeUnit.DAYS.toMillis(10)
        val secret = "your-refresh-token-secret"  // Utilisez une clé secrète différente pour les refresh tokens
        val issuer = "http://0.0.0.0:5173" //The issuer of the token (React Native App)


        val algorithm = Algorithm.HMAC256(secret)
        val expiresAt = Date(System.currentTimeMillis() + refreshTokenExpirationMillis)

        val refreshToken = JWT.create()
            .withSubject("RefreshToken")
            .withIssuer(issuer)
            .withClaim("email", email)
            .withExpiresAt(expiresAt)
            .sign(algorithm)

        DAOUser.saveRefreshToken(email, refreshToken)
        return refreshToken
    }

    val failedLoginAttempts = mutableMapOf<String, Int>()
    val failedRegisterAttempts = mutableMapOf<String, Int>()
    val blockedIPs = mutableMapOf<String, Long>()

    fun ApplicationCall.getClientIP(): String = request.origin.remoteHost

    fun String.isBlocked(): Boolean {
        val blockExpiration = blockedIPs[this] ?: return false
        return System.currentTimeMillis() < blockExpiration
    }

    fun MutableMap<String, Int>.incrementAndGet(key: String): Int {
        val attempts = getOrDefault(key, 0) + 1
        this[key] = attempts
        return attempts
    }

    fun blockIP(clientIP: String) {
        val blockDuration = TimeUnit.MINUTES.toMillis(BLOCK_DURATION_MINUTES.toLong())
        val blockExpiration = System.currentTimeMillis() + blockDuration
        blockedIPs[clientIP] = blockExpiration
        println("Blocked IP address: $clientIP for $BLOCK_DURATION_MINUTES minutes")
    }

    routing {
        post("/login") {

            val clientIP = call.getClientIP()
            if (clientIP.isBlocked()) {
                call.respond(HttpStatusCode.Forbidden, "IP address blocked. Please try again later.")
                return@post
            }

            val loginRequest = call.receive<LoginRequest>()
            val user = DAOUser.getUserByEmail(loginRequest.email)

            if ((user != null) && (user.password == loginRequest.password)) {
                failedLoginAttempts.remove(clientIP)
                println("User found: $user")
                val token = application.generateToken(user.toString())
                val refreshToken = application.generateRefreshToken(user.email)
                call.respond(HttpStatusCode.OK, LoginResponse(token, refreshToken))

            } else {
                val attempts = failedLoginAttempts.incrementAndGet(clientIP)
                if (attempts >= MAX_LOGIN_ATTEMPTS) {
                    blockIP(clientIP)
                }
                call.respond(HttpStatusCode.Unauthorized, "Username or password incorrect.")
            }
        }

        post("/refresh-token") {
            val refreshRequest = call.receive<RefreshTokenRequest>()
            val email = DAOUser.getEmailByRefreshToken(refreshRequest.refreshToken)
            if (email != null) {
                val newToken = application.generateRefreshToken(email)
                call.respond(HttpStatusCode.OK, TokenResponse(newToken))
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid refresh token")
            }
        }
    }
}

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val refreshToken: String)
data class RefreshTokenRequest(val refreshToken: String)
data class TokenResponse(val token: String)