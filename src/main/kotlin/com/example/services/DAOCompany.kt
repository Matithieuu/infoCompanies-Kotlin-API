package com.example.services

import com.example.data.Companies
import com.example.data.Company
import com.example.data.CompanyEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOCompany {

    companion object {

        private fun transformEntityToCompany(companyEntity: CompanyEntity): Company {
            return Company(
                companyEntity.id.value,
                companyEntity.denomination,
                companyEntity.siren,
                companyEntity.nic,
                companyEntity.formeJuridique,
                companyEntity.codeAPE,
                companyEntity.adresse,
                companyEntity.codePostal,
                companyEntity.ville,
                companyEntity.region,
                companyEntity.dateImmatriculation,
                companyEntity.dateRadiation,

                companyEntity.dateClotureExercice1_2019,
                companyEntity.CA1_2019,
                companyEntity.resultat1_2019,
                companyEntity.dateClotureExercice2_2019,
                companyEntity.CA2_2019,
                companyEntity.resultat2_2019,
                companyEntity.dateClotureExercice3_2019,
                companyEntity.CA3_2019,
                companyEntity.resultat3_2019,

                companyEntity.dateClotureExercice1_2020,
                companyEntity.CA1_2020,
                companyEntity.resultat1_2020,
                companyEntity.dateClotureExercice2_2020,
                companyEntity.CA2_2020,
                companyEntity.resultat2_2020,
                companyEntity.dateClotureExercice3_2020,
                companyEntity.CA3_2020,
                companyEntity.resultat3_2020,

                companyEntity.dateClotureExercice1_2021,
                companyEntity.CA1_2021,
                companyEntity.resultat1_2021,
                companyEntity.dateClotureExercice2_2021,
                companyEntity.CA2_2021,
                companyEntity.resultat2_2021,
                companyEntity.dateClotureExercice3_2021,
                companyEntity.CA3_2021,
                companyEntity.resultat3_2021,

                companyEntity.dateClotureExercice1_2022,
                companyEntity.CA1_2022,
                companyEntity.resultat1_2022,
                companyEntity.dateClotureExercice2_2022,
                companyEntity.CA2_2022,
                companyEntity.resultat2_2022,
                companyEntity.dateClotureExercice3_2022,
                companyEntity.CA3_2022,
                companyEntity.resultat3_2022,

                companyEntity.dateClotureExercice1,
                companyEntity.CA1,
                companyEntity.resultat1,
                companyEntity.dateClotureExercice2,
                companyEntity.CA2,
                companyEntity.resultat2,
                companyEntity.dateClotureExercice3,
                companyEntity.CA3,
                companyEntity.resultat3,

                companyEntity.secteurActivite,
                companyEntity.phone,
                companyEntity.website,
                companyEntity.reviews,
                companyEntity.schedule,
                companyEntity.instagram,
                companyEntity.facebook,
                companyEntity.twitter,
                companyEntity.linkedin,
                companyEntity.youtube,
                companyEntity.email,
                companyEntity.dateOfScrapping,
            )
        }

        fun getCompanyBySiren(siren: String): Company? = transaction {
            CompanyEntity.find { Companies.siren eq siren }
                .firstOrNull()?.let { companyEntity ->
                    transformEntityToCompany(companyEntity)
                }
        }

        fun get10CompaniesBySearching(searchingTerms: String): List<Company> = transaction {
            CompanyEntity.find { Companies.denomination like "%$searchingTerms%" }.limit(10)
                .map { companyEntity ->
                    transformEntityToCompany(companyEntity)
                }
        }

        fun getCompaniesBySearchingOptions(params: Map<String, String>, page: Int, pageSize: Int = 10): Pair<List<Company>, Long> = transaction {
            // Start with a base query
            var query = Companies.selectAll()

            // Build the query based on the params
            for ((key, value) in params) {
                val predicate = when (key) {
                    "region" -> Companies.region eq value
                    "ville" -> Companies.ville eq value
                    // Add more cases as needed
                    else -> null
                }

                predicate?.let {
                    query = query.andWhere { it }
                }
            }

            // Count total available records
            val totalRecords = query.count()

            // Calculate total pages
            val totalPages = if ((totalRecords % pageSize).toInt() == 0) {
                totalRecords / pageSize
            } else {
                totalRecords / pageSize + 1
            }

            // Apply limit and offset for pagination
            val companies = query
                .limit(pageSize, ((page - 1) * pageSize).toLong())
                .map { row ->
                    transformEntityToCompany(CompanyEntity.wrapRow(row))
                }

            // Return the list of companies and the total number of pages
            Pair(companies, totalPages)
        }
    }
}