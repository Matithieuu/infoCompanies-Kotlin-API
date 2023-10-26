package com.example.routing

import com.example.applicationHttpClient
import com.example.data.Company
import com.example.data.UserSessionOAuth
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.DAOCompany
import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*


fun Application.configureCompanyRoutes(httpClient: HttpClient = applicationHttpClient) {
    routing {
        authenticate("auth-oauth-google") {
            get("getAllCompanies") {
                val userSessionOAuth: UserSessionOAuth? = call.sessions.get()
                if (userSessionOAuth != null) {
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
}
