package com.example.plugins

import com.example.applicationHttpClient
import com.example.data.UserSession
import com.example.data.UserSessionOAuth
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

val redirects = mutableMapOf<String, String>()

fun Application.configureSecurity(httpClient: HttpClient = applicationHttpClient) {
    install(Sessions) {
        cookie<UserSessionOAuth>("user_session-oauth")
        cookie<UserSession>("user_session") {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 * 24 * 365
        }
    }

    install(Authentication) {
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

        session<UserSession>("auth-session") {
            validate { session ->
                if(session.name.startsWith("mat")) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("http://localhost:5173/login")
            }
        }
    }
}