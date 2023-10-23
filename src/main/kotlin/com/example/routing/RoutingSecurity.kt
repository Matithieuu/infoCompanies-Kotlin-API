package com.example.routing

import com.example.applicationHttpClient
import com.example.data.UserInfo
import com.example.data.UserSession
import com.example.plugins.redirects
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.client.request.*


fun Application.configureOAuthRoutes(httpClient: HttpClient = applicationHttpClient) {
    routing {
        authenticate("auth-oauth-google") {
            get("/login") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                call.sessions.set(UserSession(principal!!.state!!, principal.accessToken))
                val redirect = redirects[principal.state!!]
                call.respondRedirect(redirect!!)
            }
        }

        get("/logout") {
            call.sessions.clear<UserSession>()
            call.respondRedirect("/")
        }

        get("/me") {
            val userSession: UserSession? = call.sessions.get()
            if (userSession != null) {
                val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${userSession.token}")
                    }
                }.body()
                call.respondText("Hello, $userInfo!")
            } else {
                println("Pas de session")
                println()
                val redirectUrl = URLBuilder("http://127.0.0.1:8080/").run {
                    parameters.append("redirectUrl", call.request.uri)
                    build()
                }
                call.respondRedirect(redirectUrl)
            }
        }
    }
}
