package com.example.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.DAOCompany
import io.ktor.http.*

fun Application.configureCompanyRoutes() {
    routing {
        //authenticate("auth_jwt") {
            get("Company/{siren}") {
                val siren = call.parameters["siren"]
                val textToDisplay = DAOCompany.getCompanyBySiren(siren!!)
                println(textToDisplay)
                call.respondText(textToDisplay.toString())
            }

            get ("SearchCompany/{search}") {
                val search = call.parameters["search"]
                val textToDisplay = DAOCompany.get10CompaniesBySearching(search!!)
                println(textToDisplay)
                call.respondText(textToDisplay.toString())
            }

            get("SortCompany/{sortedOptions}") {
                val query = call.request.queryParameters["sortedOptions"]
                val regEx = Regex("[^A-Za-z0-9 ]")
                val searchKeyWord = regEx.replace(query!!, "")

                // Assuming sortedOptions is a string like "region:Languedoc-Roussillon-Midi-Pyrénées,ville:OLARGUES"

                val optionsMap = searchKeyWord.split(",").associate {
                    it.split(":").let { pair ->
                        pair.first() to pair.last()
                    }
                }

                val companies = DAOCompany.getCompaniesBySearchingOptions(optionsMap)
                call.respondText(companies.toString(), ContentType.Text.Plain)
            }
        //}
    }
}
