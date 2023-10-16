package com.example.services

import com.example.data.Company
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOCompany  {

    companion object {
        fun getAllCompanies() = transaction {
            Company.selectAll().map { it[Company.name] }
        }

        fun createCompany(name: String, address: String) = transaction {
            Company.insert {
                it[Company.name] = name
                it[Company.address] = address
            }
        }
        fun getCompanyById(id: Int) = transaction {
            Company.select { Company.id eq id }.map { it[Company.name] }
        }

        fun updateCompany(id: Int, name: String, address: String) = transaction {
            Company.update({ Company.id eq id }) {
                it[Company.name] = name
                it[Company.address] = address
            }
        }

        fun deleteCompany(id: Int) = transaction {
            Company.deleteWhere { Company.id eq id }
        }
    }
}