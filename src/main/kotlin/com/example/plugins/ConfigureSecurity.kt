package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.applicationHttpClient
import com.example.data.UserSessionOAuth
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

val redirects = mutableMapOf<String, String>()

fun Application.configureSecurity(httpClient: HttpClient = applicationHttpClient) {
    install(Sessions) {
        cookie<UserSessionOAuth>("user_session-oauth") {
            cookie.maxAgeInSeconds = 60 * 60 * 24 * 365
            cookie.extensions["SameSite"] = "lax"
        }
    }
    val myRealm = "Access to the application " //The description of the page
    val secret = "your-secret-key" //Encryption key
    val issuer = "http://0.0.0.0:5173" //The issuer of the token (React Native App)
    val audience = "http://127.0.0.1:8080/login" //The page where the token is sent to

    install(Authentication) {
        jwt("auth_jwt"){
            realm = myRealm
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build())
            validate { credential ->
                val username = credential.payload.getClaim("username").asString()
                println("User connecting: $username")
                if (username != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }

        oauth("auth-oauth-google") {
            urlProvider = { "http://127.0.0.1:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "google",
                    authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                    accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv("GOOGLE_CLIENT_ID"),
                    clientSecret = System.getenv("GOOGLE_CLIENT_SECRET"),
                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                    extraAuthParameters = listOf("access_type" to "offline"),
                    onStateCreated = { call, state ->
                        redirects[state] = call.request.queryParameters["redirectUrl"]!!
                    }
                )
            }
            client = httpClient
        }
    }
}