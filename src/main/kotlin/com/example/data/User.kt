package com.example.data

import org.jetbrains.exposed.sql.*


data class User(
    val id : Int,
    val name: String,
    val email: String,
    val password: String,
    val address: String,
    val phone: String,
    val refreshToken : String
    )
object Users : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val address = varchar("address", 255)
    val phone = varchar("phone", 255)
    val refreshToken = varchar("refreshToken", 255)

    override val primaryKey = PrimaryKey(id)
}
