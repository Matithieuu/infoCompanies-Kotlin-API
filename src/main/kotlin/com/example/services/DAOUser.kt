package com.example.services

import com.example.data.User
import com.example.data.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOUser  {

    companion object {
        fun getAllUsers() = transaction {
            Users.selectAll().map { it[Users.name] }
        }

        fun addUser(name: String, email: String, password: String, address: String, phone: String) = transaction {
            Users.insert {
                it[Users.name] = name
                it[Users.email] = email
                it[Users.password] = password
                it[Users.address] = address
                it[Users.phone] = phone
            }
        }

        fun getUserById(id: Int) = transaction {
            Users.select { Users.id eq id }.map { it[Users.name] }
        }

        fun updateUser(id: Int, name: String, email: String, password: String, address: String, phone: String) = transaction {
            Users.update({ Users.id eq id }) {
                it[Users.name] = name
                it[Users.email] = email
                it[Users.password] = password
                it[Users.address] = address
                it[Users.phone] = phone
            }
        }

        fun deleteUser(id: Int) = transaction {
            Users.deleteWhere { Users.id eq id }
        }

        fun getUserByEmail(email: String) = transaction {
            if (Users.select { Users.email eq email }.count() > 0) {
                Users.select { Users.email eq email }.map { it[Users.email] ; it[Users.password] }
            } else {
                null
            }
        }
    }
}