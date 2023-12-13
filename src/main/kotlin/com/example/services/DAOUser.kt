package com.example.services

import com.example.data.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DAOUser  {

    companion object {

        private fun transformEntityToUser(userEntity: UserEntity): User {
            return User(
                userEntity.id.value,
                userEntity.name,
                userEntity.email,
                userEntity.password,
                userEntity.address,
                userEntity.phone,
                userEntity.refreshToken
            )
        }

       fun getAllUsers(): List<User> = transaction {
            UserEntity.all().map { userEntity ->
                transformEntityToUser(userEntity)
            }
        }

       fun findOrCreateUser(user: UserInfo): User = transaction {
            val userEntity = UserEntity.find { Users.email eq user.email }.firstOrNull()
            if (userEntity == null) {
                val newUserEntity = UserEntity.new {
                    this.name = name
                    this.email = email
                    this.password = password
                    this.address = address
                    this.phone = phone
                    this.refreshToken = refreshToken
                }
                transformEntityToUser(newUserEntity)
            } else {
                transformEntityToUser(userEntity)
            }
        }

        fun getUserById(user: User) : User? = transaction {
            val userEntity = UserEntity.find { Users.id eq user.id }.firstOrNull()
            if (userEntity == null) {
                return@transaction null
            } else {
                transformEntityToUser(userEntity)
            }
        }

        fun updateUser(user: User) = transaction {
            Users.update({ Users.id eq user.id }) {
                it[name] = user.name
                it[email] = user.email
                it[password] = user.password
                it[address] = user.address
                it[phone] = user.phone
                it[refreshToken] = user.refreshToken
            }
        }

        fun deleteUser(id: Int) = transaction {
            Users.deleteWhere { Users.id eq id }
        }

        fun getUserByEmail(email: String): User? = transaction {
            UserEntity.find { Users.email eq email }.firstOrNull()?.let { userEntity ->
                transformEntityToUser(userEntity)
            }
        }

        fun saveRefreshToken(email: String, refreshToken: String) = transaction {
            Users.update({ Users.email eq email }) {
                it[Users.refreshToken] = refreshToken
            }
        }

        fun getEmailByRefreshToken(refreshToken: String) = transaction {
            Users.select { Users.refreshToken eq refreshToken }.map { it[Users.email] }.firstOrNull()
        }
    }
}