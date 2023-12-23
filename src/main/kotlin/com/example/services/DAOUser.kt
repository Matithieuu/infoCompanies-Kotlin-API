package com.example.services

import com.example.data.*
import com.stripe.model.Customer
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
                userEntity.phone,
                userEntity.city,
                userEntity.address,
                userEntity.role,
                userEntity.provider,
                userEntity.refreshToken,
                userEntity.stripe_id
            )
        }

       fun getAllUsers(): List<User> = transaction {
            UserEntity.all().map { userEntity ->
                transformEntityToUser(userEntity)
            }
        }

        fun findOrCreateUser(user: User): User = transaction {
            val userEntity = UserEntity.find { Users.email eq user.email }.firstOrNull()
            val stripeUser = DAOStripeUser.findOrCreateStripeUser(user)
            if (userEntity == null) {
                // Create a new UserEntity and potentially a new StripeUserEntity
                val newUserEntity = UserEntity.new {
                    this.name = user.name
                    this.email = user.email
                    this.password = user.password
                    this.city = user.city
                    this.address = user.address
                    this.phone = user.phone
                    this.role = user.role
                    this.provider = user.provider
                    this.refreshToken = user.refreshToken
                    this.stripe_id = stripeUser.id
                }
                transformEntityToUser(newUserEntity)
            } else {
                // Update the existing UserEntity with the new Stripe ID
                Users.update({ Users.email eq user.email }) {
                    it[Users.stripe_id] = stripeUser.id
                }
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
                it[stripe_id] = user.stripe_id
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

        fun getStripeID(user: User): Int? = transaction {
            Users.select { Users.id eq user.id }.map { it[Users.stripe_id] }.firstOrNull()
        }

        fun updateForeignKeyOfUser(user: User, stripe_id: Int) = transaction {
            Users.update({ Users.id eq user.id }) {
                it[Users.stripe_id] = stripe_id
            }
        }
    }
}