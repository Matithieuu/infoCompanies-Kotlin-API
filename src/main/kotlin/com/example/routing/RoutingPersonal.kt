package com.example.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.auth.*
import io.ktor.server.routing.get


fun Application.configurePersonalRoutes() {
    routing {
        authenticate("auth_jwt") {
            get("/hello") {
                call.respondText("Hello, Friend!")
            }
        }

//        get("/me") {
//            val userSessionOAuth: UserSessionOAuth? = call.sessions.get()
//            if (userSessionOAuth != null) {
//                val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
//                    headers {
//                        append(HttpHeaders.Authorization, "Bearer ${userSessionOAuth.token}")
//                    }
//                }.body()
//                call.respondText("Hello, $userInfo!")
//            } else {
//                println("Pas de session")
//                println()
//                val redirectUrl = URLBuilder("http://localhost:5173/login/").run {
//                    parameters.append("redirectUrl", call.request.uri)
//                    build()
//                }
//                call.respondRedirect(redirectUrl)
//            }
//        }
    }
}
