package com.example.routing

import com.example.applicationHttpClient
import com.example.data.UserInfo
import com.example.data.UserSessionOAuth
import com.example.services.DAOUser.Companion.findOrCreateUser
import com.example.utils.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.client.request.*

fun Application.configureOAuthRoutes(httpClient: HttpClient = applicationHttpClient) {
    routing {
        authenticate("auth-oauth-google") {
            get("/login-oauth") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                if (principal != null) {
                    val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                        headers {
                            append(HttpHeaders.Authorization, "Bearer ${principal.accessToken}")
                        }
                    }.body() ?: throw Exception("Cannot get user info")

                    // TODO: create an user

                    call.sessions.set(
                        UserSessionOAuth(
                            state = principal.state!!,
                            token = principal.accessToken
                        )
                    )
                    call.response.cookies.append(Cookie("AuthToken", generateRefreshToken(userInfo.email),
                        secure = false,
                        httpOnly = true,
                        domain = "http://localhost:5173/",
                        ))

                    call.respondRedirect("http://localhost:5173/dashboard")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Échec de l'authentification")
                }
            }


            get("/logout") {
                call.sessions.clear<UserSessionOAuth>()
                call.respondRedirect("http://localhost:5173/login")
            }
        }
    }
}