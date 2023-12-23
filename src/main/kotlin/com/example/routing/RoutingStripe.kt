package com.example.routing

import com.example.data.UserEntity
import com.example.data.Users
import com.example.services.DAOStripeUser
import com.example.services.DAOUser
import com.stripe.model.Customer
import com.stripe.model.PaymentIntent
import com.stripe.net.RequestOptions
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.h2.engine.User

//Todo : put the key in a config file
val client_public = "pk_test_51OMa7QKjCboMtBPjlKO24K5aYRfF8d0y2YrjgwjPGEFYMeweeeTRRCCQdfD3TdjzTG3UoUdFKABg9FvUT2WDSSHd00ycBKk8fu"
val mutex = Mutex()

// back : https://stripe.com/docs/api/customers/create?shell=true&api=true&resource=customers&action=create
// front : https://stripe.com/docs/js/payment_intents/

fun Application.configureRoutingStripe() {
    routing {
        post("/create-payment-intent") {
            mutex.withLock {
                val userRequest = call.receive<UserRequest>()
                println("loginRequest : $userRequest")
                val user = DAOUser.getUserByEmail(userRequest.email)
                println("user : $user")

                if (user != null) {
                    DAOStripeUser.findOrCreateStripeUser(user)
                    println("user.stripe_id : ${user.stripe_id}")
                    val stripeID = DAOStripeUser.getStripeUserById(user.stripe_id)
                    println("stripeID : $stripeID")
                    val customer = Customer.retrieve(stripeID?.stripe_id)

                    // The idempotency key is used to prevent the same payment from being processed twice.
                    // The key is deleted from Stripe after 24h.
                    val requestOptions = RequestOptions.RequestOptionsBuilder()
                        .setIdempotencyKey("user_${user.id}")
                        .build()

                    val paymentIntent = PaymentIntent.create(
                        mapOf(
                            // Parameters plays a role in the security of the payment
                            "currency" to "eur",
                            "amount" to 30 * 100 * 10, // It is in cents, so 30€
                            "description" to "monthly subscription",
                            "customer" to customer.id,
                        ), requestOptions
                    )
                    call.respond(mapOf("clientSecret" to paymentIntent.clientSecret))
                }
            }
        }

        get("/config") {
            call.respond(mapOf("publishableKey" to client_public))
        }
    }
}

data class UserRequest(
    val email: String
)