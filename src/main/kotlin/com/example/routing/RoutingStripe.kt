package com.example.routing

import com.stripe.model.PaymentIntent
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
val client_public = "pk_test_51OMa7QKjCboMtBPjlKO24K5aYRfF8d0y2YrjgwjPGEFYMeweeeTRRCCQdfD3TdjzTG3UoUdFKABg9FvUT2WDSSHd00ycBKk8fu"

fun Application.configureRoutingStripe() {
    routing {
        post("/create-payment-intent") {
            val paymentIntent = PaymentIntent.create(
                mapOf(
                    // Parameters plays a role in the security of the payment
                    "currency" to "eur",
                    "amount" to 3000,
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
