package com.example.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*

data class CompanyData(
    val id: Int,
    val name: String,
    val address: String
)

object Company : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val address = varchar("address", 255)

    override val primaryKey = PrimaryKey(id)

}
