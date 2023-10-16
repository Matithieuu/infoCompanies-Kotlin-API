package com.example.plugins

import com.example.data.Company
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.DAOCompany
import io.ktor.server.request.*

fun Application.configureRouting() {
    routing (){
        get("/") {
            call.respondText("Hello World!")
        }

        get ("getAllCompanies") {
            val textToDisplay = DAOCompany.getAllCompanies()
            call.respondText(textToDisplay.toString())
        }

        post ("addCompany") {
            val companyRequest = call.receive<Company>()
            print("Détails de la requete" + companyRequest)
            DAOCompany.createCompany(companyRequest.name.toString(), companyRequest.address.toString())
            call.respondText("Company added ! Name : ${companyRequest.name} Address : ${companyRequest.address}")
        }
    }
}
