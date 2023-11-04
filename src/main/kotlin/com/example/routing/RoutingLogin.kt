package com.example.routing

import com.example.services.DAOUser
import com.example.utils.generateRefreshToken
import com.example.utils.generateToken
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.concurrent.TimeUnit

private const val BLOCK_DURATION_MINUTES = 10
private const val MAX_LOGIN_ATTEMPTS = 3
//private const val MAX_REGISTER_ATTEMPTS = 3

fun Application.configureLoginRoutes() {

    val failedLoginAttempts = mutableMapOf<String, Int>()
    //val failedRegisterAttempts = mutableMapOf<String, Int>()
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
                val token = generateToken(user.toString())
                val refreshToken = generateRefreshToken(user.email)
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
                val newToken = generateRefreshToken(email)
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