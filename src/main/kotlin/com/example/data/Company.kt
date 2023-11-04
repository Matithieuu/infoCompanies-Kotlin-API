package com.example.data

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate
object Companies : IntIdTable() {
    val denomination = varchar("denomination", 255).nullable()
    val siren = varchar("siren", 9).nullable()
    val nic = varchar("nic", 5).nullable()
    val formeJuridique = varchar("forme_juridique", 255).nullable()
    val codeAPE = varchar("code_ape", 5).nullable()
    val adresse = text("adresse").nullable()
    val codePostal = varchar("code_postal", 5).nullable()
    val ville = varchar("ville", 255).nullable()
    val region = varchar("region", 255).nullable()
    val dateImmatriculation = date("date_immatriculation").nullable()
    val dateRadiation = date("date_radiation").nullable()

    val dateClotureExercice1_2019 = date("date_cloture_exercice_1_2019").nullable()
    val CA1_2019 = long("ca_1_2019").nullable()
    val resultat1_2019 = long("resultat_1_2019").nullable()
    val dateClotureExercice2_2019 = date("date_cloture_exercice_2_2019").nullable()
    val CA2_2019 = long("ca_2_2019").nullable()
    val resultat2_2019 = long("resultat_2_2019").nullable()
    val dateClotureExercice3_2019 = date("date_cloture_exercice_3_2019").nullable()
    val CA3_2019 = long("ca_3_2019").nullable()
    val resultat3_2019 = long("resultat_3_2019").nullable()

    val dateClotureExercice1_2020 = date("date_cloture_exercice_1_2020").nullable()
    val CA1_2020 = long("ca_1_2020").nullable()
    val resultat1_2020 = long("resultat_1_2020").nullable()
    val dateClotureExercice2_2020 = date("date_cloture_exercice_2_2020").nullable()
    val CA2_2020 = long("ca_2_2020").nullable()
    val resultat2_2020 = long("resultat_2_2020").nullable()
    val dateClotureExercice3_2020 = date("date_cloture_exercice_3_2020").nullable()
    val CA3_2020 = long("ca_3_2020").nullable()
    val resultat3_2020 = long("resultat_3_2020").nullable()

    val dateClotureExercice1_2021 = date("date_cloture_exercice_1_2021").nullable()
    val CA1_2021 = long("ca_1_2021").nullable()
    val resultat1_2021 = long("resultat_1_2021").nullable()
    val dateClotureExercice2_2021 = date("date_cloture_exercice_2_2021").nullable()
    val CA2_2021 = long("ca_2_2021").nullable()
    val resultat2_2021 = long("resultat_2_2021").nullable()
    val dateClotureExercice3_2021 = date("date_cloture_exercice_3_2021").nullable()
    val CA3_2021 = long("ca_3_2021").nullable()
    val resultat3_2021 = long("resultat_3_2021").nullable()

    val dateClotureExercice1_2022 = date("date_cloture_exercice_1_2022").nullable()
    val CA1_2022 = long("ca_1_2022").nullable()
    val resultat1_2022 = long("resultat_1_2022").nullable()
    val dateClotureExercice2_2022 = date("date_cloture_exercice_2_2022").nullable()
    val CA2_2022 = long("ca_2_2022").nullable()
    val resultat2_2022 = long("resultat_2_2022").nullable()
    val dateClotureExercice3_2022 = date("date_cloture_exercice_3_2022").nullable()
    val CA3_2022 = long("ca_3_2022").nullable()
    val resultat3_2022 = long("resultat_3_2022").nullable()

    val dateClotureExercice1 = date("date_cloture_exercice_1").nullable()
    val CA1 = long("ca_1").nullable()
    val resultat1 = long("resultat_1").nullable()
    val dateClotureExercice2 = date("date_cloture_exercice_2").nullable()
    val CA2 = long("ca_2").nullable()
    val resultat2 = long("resultat_2").nullable()
    val dateClotureExercice3 = date("date_cloture_exercice_3").nullable()
    val CA3 = long("ca_3").nullable()
    val resultat3 = long("resultat_3").nullable()

    val secteurActivite = varchar("secteur_activite", 255).nullable()
}

class CompanyEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompanyEntity>(Companies)

    var denomination by Companies.denomination
    var siren by Companies.siren
    var nic by Companies.nic
    var formeJuridique by Companies.formeJuridique
    var codeAPE by Companies.codeAPE
    var adresse by Companies.adresse
    var codePostal by Companies.codePostal
    var ville by Companies.ville
    var region by Companies.region
    var dateImmatriculation by Companies.dateImmatriculation
    var dateRadiation by Companies.dateRadiation

    var dateClotureExercice1_2019 by Companies.dateClotureExercice1_2019
    var CA1_2019 by Companies.CA1_2019
    var resultat1_2019 by Companies.resultat1_2019
    var dateClotureExercice2_2019 by Companies.dateClotureExercice2_2019
    var CA2_2019 by Companies.CA2_2019
    var resultat2_2019 by Companies.resultat2_2019
    var dateClotureExercice3_2019 by Companies.dateClotureExercice3_2019
    var CA3_2019 by Companies.CA3_2019
    var resultat3_2019 by Companies.resultat3_2019

    var dateClotureExercice1_2020 by Companies.dateClotureExercice1_2020
    var CA1_2020 by Companies.CA1_2020
    var resultat1_2020 by Companies.resultat1_2020
    var dateClotureExercice2_2020 by Companies.dateClotureExercice2_2020
    var CA2_2020 by Companies.CA2_2020
    var resultat2_2020 by Companies.resultat2_2020
    var dateClotureExercice3_2020 by Companies.dateClotureExercice3_2020
    var CA3_2020 by Companies.CA3_2020
    var resultat3_2020 by Companies.resultat3_2020

    var dateClotureExercice1_2021 by Companies.dateClotureExercice1_2021
    var CA1_2021 by Companies.CA1_2021
    var resultat1_2021 by Companies.resultat1_2021
    var dateClotureExercice2_2021 by Companies.dateClotureExercice2_2021
    var CA2_2021 by Companies.CA2_2021
    var resultat2_2021 by Companies.resultat2_2021
    var dateClotureExercice3_2021 by Companies.dateClotureExercice3_2021
    var CA3_2021 by Companies.CA3_2021
    var resultat3_2021 by Companies.resultat3_2021

    var dateClotureExercice1_2022 by Companies.dateClotureExercice1_2022
    var CA1_2022 by Companies.CA1_2022
    var resultat1_2022 by Companies.resultat1_2022
    var dateClotureExercice2_2022 by Companies.dateClotureExercice2_2022
    var CA2_2022 by Companies.CA2_2022
    var resultat2_2022 by Companies.resultat2_2022
    var dateClotureExercice3_2022 by Companies.dateClotureExercice3_2022
    var CA3_2022 by Companies.CA3_2022
    var resultat3_2022 by Companies.resultat3_2022

    var dateClotureExercice1 by Companies.dateClotureExercice1
    var CA1 by Companies.CA1
    var resultat1 by Companies.resultat1
    var dateClotureExercice2 by Companies.dateClotureExercice2
    var CA2 by Companies.CA2
    var resultat2 by Companies.resultat2
    var dateClotureExercice3 by Companies.dateClotureExercice3
    var CA3 by Companies.CA3
    var resultat3 by Companies.resultat3

    var secteurActivite by Companies.secteurActivite

    override fun toString(): String {
        return super.toString()
    }

    fun toCompany() = Company(
        id.value,
        denomination,
        siren,
        nic,
        formeJuridique,
        codeAPE,
        adresse,
        codePostal,
        ville,
        region,
        dateImmatriculation,
        dateRadiation,

        dateClotureExercice1_2019,
        CA1_2019,
        resultat1_2019,
        dateClotureExercice2_2019,
        CA2_2019,
        resultat2_2019,
        dateClotureExercice3_2019,
        CA3_2019,
        resultat3_2019,

        dateClotureExercice1_2020,
        CA1_2020,
        resultat1_2020,
        dateClotureExercice2_2020,
        CA2_2020,
        resultat2_2020,
        dateClotureExercice3_2020,
        CA3_2020,
        resultat3_2020,

        dateClotureExercice1_2021,
        CA1_2021,
        resultat1_2021,
        dateClotureExercice2_2021,
        CA2_2021,
        resultat2_2021,
        dateClotureExercice3_2021,
        CA3_2021,
        resultat3_2021,

        dateClotureExercice1_2022,
        CA1_2022,
        resultat1_2022,
        dateClotureExercice2_2022,
        CA2_2022,
        resultat2_2022,
        dateClotureExercice3_2022,
        CA3_2022,
        resultat3_2022,

        dateClotureExercice1,
        CA1,
        resultat1,
        dateClotureExercice2,
        CA2,
        resultat2,
        dateClotureExercice3,
        CA3,
        resultat3,

        secteurActivite
    )
}

data class Company(
    val id: Int,
    val denomination: String?,
    val siren: String?,
    val nic: String?,
    val formeJuridique: String?,
    val codeAPE: String?,
    val adresse: String?,
    val codePostal: String?,
    val ville: String?,
    val region: String?,
    val dateImmatriculation: LocalDate?,
    val dateRadiation: LocalDate?,

    val dateClotureExercice1_2019: LocalDate?,
    val CA1_2019: Long?,
    val resultat1_2019: Long?,
    val dateClotureExercice2_2019: LocalDate?,
    val CA2_2019: Long?,
    val resultat2_2019: Long?,
    val dateClotureExercice3_2019: LocalDate?,
    val CA3_2019: Long?,
    val resultat3_2019: Long?,

    val dateClotureExercice1_2020: LocalDate?,
    val CA1_2020: Long?,
    val resultat1_2020: Long?,
    val dateClotureExercice2_2020: LocalDate?,
    val CA2_2020: Long?,
    val resultat2_2020: Long?,
    val dateClotureExercice3_2020: LocalDate?,
    val CA3_2020: Long?,
    val resultat3_2020: Long?,

    val dateClotureExercice1_2021: LocalDate?,
    val CA1_2021: Long?,
    val resultat1_2021: Long?,
    val dateClotureExercice2_2021: LocalDate?,
    val CA2_2021: Long?,
    val resultat2_2021: Long?,
    val dateClotureExercice3_2021: LocalDate?,
    val CA3_2021: Long?,
    val resultat3_2021: Long?,

    val dateClotureExercice1_2022: LocalDate?,
    val CA1_2022: Long?,
    val resultat1_2022: Long?,
    val dateClotureExercice2_2022: LocalDate?,
    val CA2_2022: Long?,
    val resultat2_2022: Long?,
    val dateClotureExercice3_2022: LocalDate?,
    val CA3_2022: Long?,
    val resultat3_2022: Long?,

    val dateClotureExercice1: LocalDate?,
    val CA1: Long?,
    val resultat1: Long?,
    val dateClotureExercice2: LocalDate?,
    val CA2: Long?,
    val resultat2: Long?,
    val dateClotureExercice3: LocalDate?,
    val CA3: Long?,
    val resultat3: Long?,

    val secteurActivite: String?
)