package com.example.routing

import com.example.data.UserEntity
import com.example.data.Users
import com.example.services.DAOUser
import com.stripe.model.Customer
import com.stripe.model.PaymentIntent
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.h2.engine.User

//Todo : put the key in a config file
val client_public = "pk_test_51OMa7QKjCboMtBPjlKO24K5aYRfF8d0y2YrjgwjPGEFYMeweeeTRRCCQdfD3TdjzTG3UoUdFKABg9FvUT2WDSSHd00ycBKk8fu"


// back : https://stripe.com/docs/api/customers/create?shell=true&api=true&resource=customers&action=create
// front : https://stripe.com/docs/js/payment_intents/

fun Application.configureRoutingStripe() {
    routing {
        post("/create-payment-intent") {
            val loginRequest = call.receive<UserRequest>()
            val user = DAOUser.getUserByEmail(loginRequest.user.email)
            if (user == null) {
                // TODO: create an stripe user
                Customer.create(
                    mapOf(
                        "description" to "My First Test Customer (created for API docs)",
                        "name" to loginRequest.user.name,
                        "email" to loginRequest.user.email,
                    )
                )
                call.respondText("User not found")
                return@post

            }
            val paymentIntent = PaymentIntent.create(
                mapOf(
                    // Parameters plays a role in the security of the payment
                    "currency" to "eur",
                    "amount" to 30 * 100 * 10, // It is in cents, so 30€
                    "description" to "monthly subscription",
                )
            )
            call.respond(mapOf("clientSecret" to paymentIntent.clientSecret))
        }

        get("/config") {
            call.respond(mapOf("publishableKey" to client_public))
        }
    }
}

data class UserRequest(
    val user: UserEntity
)