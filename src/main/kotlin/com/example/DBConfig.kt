package com.example

import com.example.data.Companies
import com.example.data.StripeUsers
import com.example.data.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

fun initDB() {
    val configPath = "src/main/resources/dbconfig.properties"
    val dbConfig = HikariConfig(configPath)
    dbConfig.jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"

    val dataSource = HikariDataSource(dbConfig)
    Database.connect(dataSource)


    createTables()
    LoggerFactory.getLogger(Application::class.simpleName).info("Initialized Database")

    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() { // do something
            dataSource.close()
        }
    })
}

private fun createTables() {
    transaction {
        SchemaUtils.create(Companies)
        SchemaUtils.create(Users)
        SchemaUtils.create(StripeUsers)
    }
}

