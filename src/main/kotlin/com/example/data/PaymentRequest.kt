package com.example.data

data class PaymentRequest(
    val amount: Long, // Montant en centimes
    val currency: String, // Code de devise, par exemple "eur" pour l'Euro
    // Ajoutez d'autres propriétés selon les besoins de votre application
    val email: String,
    val name: String,
    val address: String,
    val city: String,
    val phone: String,
)
