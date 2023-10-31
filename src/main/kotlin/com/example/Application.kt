package com.example


import com.example.plugins.configureSecurity
import com.example.routing.configureCompanyRoutes
import com.example.routing.configureLoginRoutes
import com.example.routing.configureOAuthRoutes
import com.example.routing.configurePersonalRoutes
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import jdk.jfr.Enabled


fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1", module = Application::module)
            .start(wait = true)
}

val applicationHttpClient = HttpClient(CIO) {
    install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
        json()
    }
}

fun Application.module(httpClient: HttpClient = applicationHttpClient) {

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowMethods)
        allowHeader(HttpHeaders.AccessControlAllowCredentials)

        allowCredentials = true
        allowNonSimpleContentTypes = true

    }
    install(ContentNegotiation) {
        gson ()
    }

    initDB()

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Page Not Found", status = status)
        }
        status (HttpStatusCode.Unauthorized) { call, status ->
            call.respondText(text = "401: Unauthorized", status = status)
        }
        status (HttpStatusCode.Forbidden) { call, status ->
            call.respondText(text = "403: Forbidden", status = status)
        }
        status (HttpStatusCode.BadRequest) { call, status ->
            call.respondText(text = "400: Bad Request", status = status)
        }
    }

    configureSecurity(httpClient)

    configureOAuthRoutes(httpClient)

    configureLoginRoutes()

    configurePersonalRoutes()

    configureCompanyRoutes(httpClient)


}


