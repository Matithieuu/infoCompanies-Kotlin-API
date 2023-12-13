package com.example.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.example.services.DAOCompany
import io.ktor.http.*

fun Application.configureCompanyRoutes() {

    fun sanitizeInputs(input: String): String {
        val regEx = Regex("[^A-Za-z0-9 ]")
        return regEx.replace(input, "")
    }


    routing {
        //authenticate("auth_jwt") {
            get("Company/{siren}") {
                val siren = call.parameters["siren"]
                sanitizeInputs(siren!!)
                val textToDisplay = DAOCompany.getCompanyBySiren(siren)
                println(textToDisplay)
                call.respondText(textToDisplay.toString())
            }

            get ("SearchCompany/{search}") {
                val search = call.parameters["search"]
                sanitizeInputs(search!!)
                val textToDisplay = DAOCompany.get10CompaniesBySearching(search)
                println(textToDisplay)
                call.respondText(textToDisplay.toString())
            }

            get("SortCompany/{sortedOptions}") {
                val sortedOptions = call.parameters["sortedOptions"]
                requireNotNull(sortedOptions) { "Sorted options are required" }

                sanitizeInputs(sortedOptions)

                // Assuming sortedOptions is a string like "region:Languedoc-Roussillon-Midi-Pyrénées,ville:OLARGUES"
                val optionsMap = sortedOptions.split(",").associate {
                    it.split(":").let { pair ->
                        pair.first() to pair.last()
                    }
                }

                // Extract page and pageSize from query parameters if present
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: 10

                // Retrieve the paginated list of companies and the total number of pages
                val (companies, totalPages) = DAOCompany.getCompaniesBySearchingOptions(optionsMap, page, pageSize)

                // Respond with the list of companies and pagination info
                call.respond(mapOf("companies" to companies, "totalPages" to totalPages))

                // https://chat.openai.com/share/36656682-a5bf-41e1-a3ca-85bd03c87ce4
            }
        //}
    }
}
