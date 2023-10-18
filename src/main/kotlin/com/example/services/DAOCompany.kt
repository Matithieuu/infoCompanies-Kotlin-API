package com.example.services

import com.example.data.Companies
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOCompany  {

    companion object {
        fun getAllCompanies() = transaction {
            Companies.selectAll().map { it[Companies.name] }
        }

        fun addCompany(name: String, address: String) = transaction {
            Companies.insert {
                it[Companies.name] = name
                it[Companies.address] = address
            }
        }
        fun getCompanyById(id: Int) = transaction {
            Companies.select { Companies.id eq id }.map { it[Companies.name] }
        }

        fun updateCompany(id: Int, name: String, address: String) = transaction {
            Companies.update({ Companies.id eq id }) {
                it[Companies.name] = name
                it[Companies.address] = address
            }
        }

        fun deleteCompany(id: Int) = transaction {
            Companies.deleteWhere { Companies.id eq id }
        }
    }
}