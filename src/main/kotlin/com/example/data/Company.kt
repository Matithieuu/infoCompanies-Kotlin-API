package com.example.data

import org.jetbrains.exposed.sql.*


data class Company(
    val id : Int,
    val name: String,
    val address: String
)
object Companies : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val address = varchar("address", 255)

    override val primaryKey = PrimaryKey(id)

}
