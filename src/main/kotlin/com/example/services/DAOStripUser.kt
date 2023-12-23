package com.example.services

import com.example.data.*
import com.stripe.exception.StripeException
import com.stripe.model.Customer
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class DAOStripeUser {

    companion object {

        private fun transformEntityToStripeUser(userStripeEntity: StripeUserEntity): StripeUser {
            return StripeUser(
                userStripeEntity.id.value,
                userStripeEntity.stripe_id,
            )
        }

        fun findOrCreateStripeUser(user: User): StripeUser = transaction() {
            runBlocking {
                try {
                    // Check if user already has a Stripe ID
                    val fkUserStripeID = DAOUser.getStripeID(user) // FK = foreign key
                    println("existingStripeId : $fkUserStripeID")

                    // If user already has a Stripe ID, retrieve the Stripe customer
                    try {
                        if (fkUserStripeID != -1 && fkUserStripeID != null) {
                            // Search the StripeID in the database
                            val stripeID = getStripeUserById(fkUserStripeID)
                            if (stripeID != null) {
                                println("User exists with the key : $stripeID")
                                val stripeCustomer = Customer.retrieve(stripeID.stripe_id)
                                println("stripeCustomer : $stripeCustomer")
                                return@runBlocking StripeUser(0, stripeCustomer.id)
                            }
                        }
                    } catch (e: StripeException) {
                        // Handle Stripe API exceptions
                        throw RuntimeException("Failed to retrieve Stripe user")
                    }

                    // Create a new Stripe customer
                    val customerParams = mapOf(
                        "name" to user.name,
                        "email" to user.email
                    )
                    // Create a new Stripe customer and retrieve the new Stripe ID
                    try {
                        println("Creating new Stripe user")
                        val newCustomer = Customer.create(customerParams)
                        // Create a new StripeUserEntity with the new Stripe ID
                        val newStripeUserEntity = StripeUserEntity.new {
                            this.stripe_id = newCustomer.id
                        }
                        DAOUser.updateForeignKeyOfUser(user, newStripeUserEntity.id.value)
                        return@runBlocking transformEntityToStripeUser(newStripeUserEntity)

                    } catch (e: StripeException) {
                        // Handle Stripe API exceptions
                        throw RuntimeException("Failed to create Stripe user", e)
                    }

                } catch (e: StripeException) {
                    // Handle Stripe API exceptions
                    throw RuntimeException("Failed to create or retrieve Stripe user", e)
                }
            }
        }

        fun getStripeUserById(id: Int): StripeUser? = transaction {
            val stripeUserEntity = StripeUserEntity.find { StripeUsers.id eq id }.firstOrNull()
            if (stripeUserEntity == null) {
                null
            } else {
                transformEntityToStripeUser(stripeUserEntity)
            }
        }
    }
}
