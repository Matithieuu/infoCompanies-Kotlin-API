package com.example.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

data class User (
    val id : Int,
    val name: String,
    val email: String,

    // TODO : Remove password from User data class and put it in a separate data class and in another database
    val password: String,
    val phone: String,
    val city: String,
    val address: String,
    val role: String,
    val provider: String,

    // TODO : Remove refreshToken from User data class and put it in a separate data class and in another database
    val refreshToken : String,
    val stripe_id : Int,
    )

class UserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserEntity>(Users)

    var name by Users.name
    var email by Users.email
    var password by Users.password
    var phone by Users.phone
    var city by Users.city
    var address by Users.address
    var role by Users.role
    var provider by Users.provider
    var refreshToken by Users.refreshToken
    var stripe_id by Users.stripe_id

    override fun toString(): String {
        return super.toString()
    }

    fun toUser() = User(
        id.value,
        name,
        email,
        password,
        phone,
        city,
        address,
        role,
        provider,
        refreshToken,
        stripe_id
    )
}

object Users : IntIdTable() {
    val name = varchar("name", 50)
    val email = varchar("email", 50)
    val password = varchar("password", 50)
    val phone = varchar("phone", 50)
    val city = varchar("city", 50)
    val address = varchar("address", 50)
    val role = varchar("role", 50)
    val provider = varchar("provider", 50)
    val refreshToken = varchar("refreshToken", 50)
    val stripe_id = integer("stripe_id")
}
