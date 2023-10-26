package com.example.routing

import com.example.applicationHttpClient
import com.example.data.Company
import com.example.data.UserInfo
import com.example.data.UserSession
import com.example.data.UserSessionOAuth
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.DAOCompany
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.routing.get
import io.ktor.server.sessions.*


fun Application.configurePersonalRoutes(httpClient: HttpClient = applicationHttpClient) {
    routing {
        authenticate("auth-session") {
            get("/hello") {
                val userSession = call.principal<UserSession>()
                call.sessions.set(userSession?.copy(count = userSession.count + 1))
                call.respondText("Hello, ${userSession?.name}! Visit count is ${userSession?.count}.")
            }
        }

        get("/me") {
            val userSessionOAuth: UserSessionOAuth? = call.sessions.get()
            if (userSessionOAuth != null) {
                val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${userSessionOAuth.token}")
                    }
                }.body()
                call.respondText("Hello, $userInfo!")
            } else {
                println("Pas de session")
                println()
                val redirectUrl = URLBuilder("http://localhost:5173/login/").run {
                    parameters.append("redirectUrl", call.request.uri)
                    build()
                }
                call.respondRedirect(redirectUrl)
            }
        }
    }
}
