package com.example.routing

import com.example.applicationHttpClient
import com.example.data.UserInfo
import com.example.data.UserSession
import com.example.data.UserSessionOAuth
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
            get("/login-oauth") {
                // Redirects to 'authorizeUrl' automatically
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                call.sessions.set(UserSessionOAuth(principal!!.state!!, principal.accessToken))
                call.respondRedirect("http://localhost:5173/dashboard")
                //val redirect = redirects[principal.state!!]
                //call.respondRedirect(redirect!!)
            }
        }

        get("/logout") {
            call.sessions.clear<UserSessionOAuth>()
            call.sessions.clear<UserSession>()
        }
    }
}
