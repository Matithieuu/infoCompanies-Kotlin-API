package com.example.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*


data class User (
    val id : Int,
    val name: String,
    val email: String,
    val password: String,
    val address: String,
    val phone: String,
    val refreshToken : String
    )

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)

    var name by Users.name
    var email by Users.email
    var password by Users.password
    var address by Users.address
    var phone by Users.phone
    var refreshToken by Users.refreshToken

    override fun toString(): String {
        return super.toString()
    }

    fun toUser() = User(
        id.value,
        name,
        email,
        password,
        address,
        phone,
        refreshToken
    )
}




object Users : IntIdTable() {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
    val address = varchar("address", 255)
    val phone = varchar("phone", 255)
    val refreshToken = varchar("refreshToken", 255)
}
