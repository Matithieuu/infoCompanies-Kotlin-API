package com.example


import com.example.plugins.*
import com.example.routing.configureCompanyRoutes
import com.example.routing.configureOAuthRoutes
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.plugins.cors.routing.*


fun main() {
    System.setProperty("io.ktor.development", "true")

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
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Post)

        allowCredentials = true
        allowNonSimpleContentTypes = true
    }
    initDB()

    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Page Not Found", status = status)
        }
    }

    configureSecurity(httpClient)

    configureOAuthRoutes(httpClient)

    configureCompanyRoutes(httpClient)
}


