package com.example.plugins

import com.example.data.Company
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.DAOCompany
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*


fun Application.configureRouting() {
    routing {
        authenticate("auth-oauth-google") {
            get("/") {
                call.respondText("Hello World!")
            }

            get("getAllCompanies") {
                val userIpAddress = call.request.origin.remoteHost
                print("Ip address : $userIpAddress")
                val textToDisplay = DAOCompany.getAllCompanies()
                call.respondText(textToDisplay.toString())

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
