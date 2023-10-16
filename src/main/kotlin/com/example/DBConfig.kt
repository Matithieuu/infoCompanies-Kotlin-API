package com.example

import com.example.data.Company
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

fun initDB() {
    val configPath = "/home/mathieu/IdeaProjects/ktor-sample/src/main/resources/dbconfig.properties"
    val dbConfig = HikariConfig(configPath)

    val dataSource = HikariDataSource(dbConfig)
    Database.connect(dataSource)

    createTables()
    LoggerFactory.getLogger(Application::class.simpleName).info("Initialized Database")
}

private fun createTables() {
    transaction {
        SchemaUtils.create(Company)
    }
}