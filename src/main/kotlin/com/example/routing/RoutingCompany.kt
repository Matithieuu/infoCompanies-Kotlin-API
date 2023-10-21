package com.example.routing

import com.example.applicationHttpClient
import com.example.data.Company
import com.example.data.UserInfo
import com.example.data.UserSession
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.DAOCompany
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.headers
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*


fun Application.configureCompanyRoutes(httpClient: HttpClient = applicationHttpClient) {
    routing {
        get("getAllCompanies") {
            val userSession: UserSession? = call.sessions.get()
            if (userSession != null) {
                val textToDisplay = DAOCompany.getAllCompanies()
                call.respondText(textToDisplay.toString())

            } else {
                val redirectUrl = URLBuilder("http://127.0.0.1:8080/login").run {
                    parameters.append("redirectUrl", call.request.uri)
                    build()
                }
                call.respondRedirect(redirectUrl)

            }
        }

        get("Company/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            val textToDisplay = DAOCompany.getCompanyById(id!!)
            call.respondText(textToDisplay.toString())
        }

        post("addCompany") {
            print("Requete reçue")
            val companyRequest = call.receive<Company>()
            DAOCompany.addCompany(companyRequest.name, companyRequest.address)
            call.respondText("Company added ! Name : ${companyRequest.name} Address : ${companyRequest.address}")
        }

        delete("deleteCompany/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            DAOCompany.deleteCompany(id!!)
            call.respondText("Company deleted !")
        }
    }
}
