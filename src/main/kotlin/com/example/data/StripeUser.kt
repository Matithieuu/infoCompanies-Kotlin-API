package com.example.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


data class StripeUser (
    val id : Int,
    val stripe_id : String,
)

class StripeUserEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<StripeUserEntity>(StripeUsers)

    var stripe_id by StripeUsers.stripe_id

    override fun toString(): String {
        return super.toString()
    }

    fun toStripeUser() = StripeUser(
        id.value,
        stripe_id,
    )
}

object StripeUsers : IntIdTable() {
    val stripe_id = varchar("stripe_id", 100)

}